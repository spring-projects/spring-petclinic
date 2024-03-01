package org.springframework.samples.petclinic.pettypes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.samples.petclinic.owner.PetType;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.kms.KmsClient;
import software.amazon.awssdk.services.kms.model.DecryptResponse;
import software.amazon.awssdk.services.s3.S3Client;

import java.util.List;
import java.util.Optional;

/**
 * Perform some initializing of the supported pet types on startup by downloading them
 * from S3, if enabled
 */
@Component
public class InitPetTypes implements InitializingBean {

	private final Logger logger = LoggerFactory.getLogger(InitPetTypes.class);

	private final PetTypesRepository petTypesRepository;

	private final KmsClient kmsClient;

	private final S3Client s3Client;

	@Value("${app.init.pet-types.key:petclinic-pettypes.txt}")
	private String petTypesInitObjectKey;

	@Value("${app.init.pet-types.kms-encrypted:false}")
	private Boolean petTypesInitObjectKmsEncrypted;

	InitPetTypes(PetTypesRepository petTypesRepository, Optional<AwsCredentialsProvider> awsCredentialsProvider,
			Optional<S3Client> s3Client) {
		this.petTypesRepository = petTypesRepository;
		this.s3Client = s3Client.orElse(null);

		AwsCredentialsProvider credentialsProvider = awsCredentialsProvider.orElse(null);
		this.kmsClient = credentialsProvider != null
				? KmsClient.builder().credentialsProvider(credentialsProvider).build() : null;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (s3Client == null) {
			logger.info("No S3Client configured, skipping loading pettypes.");
			return;
		}

		logger.info("Loading Pet types from \"spring-petclinic-init\" bucket at " + petTypesInitObjectKey);
		String fileContents = s3Client
			.getObjectAsBytes(b -> b.bucket("spring-petclinic-init").key(petTypesInitObjectKey))
			.asUtf8String();

		// if kms encrypted, attempt to decrypt it
		if (petTypesInitObjectKmsEncrypted) {
			logger.info("Decrypting pet types using KMS key...");
			SdkBytes encryptedData = SdkBytes.fromUtf8String(fileContents);
			DecryptResponse decryptResponse = kmsClient
				.decrypt(b -> b.ciphertextBlob(encryptedData).keyId("spring-petclinic-init"));
			fileContents = decryptResponse.plaintext().asUtf8String();
		}

		List<PetType> foundTypes = fileContents.lines().map(s -> {
			PetType type = new PetType();
			type.setName(s);
			return type;
		}).toList();
		logger.info("Found " + foundTypes.size() + " pet types");

		// load the found types into the database
		if (!foundTypes.isEmpty()) {
			petTypesRepository.saveAllAndFlush(foundTypes);

			// clean up the file if we've successfully loaded from it
			logger.info("Deleting Pet types file from S3");
			s3Client.deleteObject(builder -> builder.bucket("spring-petclinic-init").key(petTypesInitObjectKey));
		}
	}

}
