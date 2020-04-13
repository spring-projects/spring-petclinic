ace.define("ace/snippets/groovy",["require","exports","module"], function(require, exports, module) {
"use strict";

var groovySnippets =
"# if\n\
snippet if\n\
	if (${1:true}) {\n\
		${0}\n\
	}\n\
# if ... else\n\
snippet ife\n\
	if (${1:true}) {\n\
		${2}\n\
	} else {\n\
		${0}\n\
	}\n\
# tertiary conditional\n\
snippet ter\n\
	${1:/* condition */} ? ${2:a} : ${3:b}\n\
# switch\n\
snippet switch\n\
	switch (${1:expression}) {\n\
		case '${3:case}':\n\
			${4:// code}\n\
			break;\n\
		${5}\n\
		default:\n\
			${2:// code}\n\
	}\n\
# case\n\
snippet case\n\
	case '${1:case}':\n\
		${2:// code}\n\
		break;\n\
	${3}\n\
\n\
# while (...) {...}\n\
snippet wh\n\
	while (${1:/* condition */}) {\n\
		${0:/* code */}\n\
	}\n\
# try\n\
snippet try\n\
	try {\n\
		${0:/* code */}\n\
	} catch (e) {}\n\
# do...while\n\
snippet do\n\
	do {\n\
		${2:/* code */}\n\
	} while (${1:/* condition */});\n\
# return\n\
snippet ret\n\
	return ${1:result}\n\
# \n\
snippet for--\n\
	for (int ${1:i} = ${2:Things}.length; ${1:i}--; ) {\n\
		${0:${2:Things}[${1:i}];}\n\
	}\n\
# for (...) {...}\n\
snippet for++\n\
	for (int ${1:i} = 0; $1 < ${2:Things}.length; $1++) {\n\
		${3:$2[$1]}$0\n\
	}\n\
";

// TODO: Would be nice to get this from the backend via an AJAX call.
// With each step contributing 1 or more snippets of code
var workflowSnippets =
"# stage\n\
snippet stage\n\
	stage('${1}')\n\
	    ${2}\n\
	}\n\
# node\n\
snippet node\n\
	node {\n\
	    ${1}\n\
	}\n\
# node-l ('label') \n\
snippet node-l\n\
	node('${1}') {\n\
	    \n\
	}\n\
# echo \n\
snippet echo\n\
	echo \"${1}\"\n\
# mail \n\
snippet mail\n\
	mail to: \"${1}\", subject: \"${2}\", body: \"${3}\"\n\
# archive \n\
snippet archive\n\
	archive '${1}'\n\
# readFile \n\
snippet readFile (from workspace)\n\
	def text = readFile '${1}'\n\
# writeFile \n\
snippet writeFile (to workspace)\n\
	writeFile file: '${1}', text: '${2}'\n\
# fileExists \n\
snippet fileExists (in workspace)\n\
	fileExists('${1}')\n\
# dir (subDir) \n\
snippet dir (subDir)\n\
	dir('${1}') {\n\
	    ${2}\n\
	}\n\
# pwd \n\
snippet pwd\n\
	pwd()\n\
# deleteDir \n\
snippet deleteDir\n\
	deleteDir()\n\
# deleteDir (subDir) \n\
snippet deleteDir (subDir)\n\
	dir('${1}') {\n\
	    deleteDir()\n\
	}\n\
# retry (count) \n\
snippet retry (count)\n\
	retry(${1:2}) {\n\
	    \n\
	}\n\
# sleep \n\
snippet sleep (millis)\n\
	sleep ${1:1000}\n\
# git \n\
snippet git \n\
	git url: '${1}', credentialsId: '${2}', branch '${3}'\n\
# input \n\
snippet input \n\
	input message: '${1:Proceed or abort?}'\n\
# input (password)\n\
snippet input  (password)\n\
	def outcome = input message: '${1}', parameters: [\n\
	    [name: 'password', description: 'Password', \\$class: 'PasswordParameterDefinition']\n\
	]\n\
# input (true/false)\n\
snippet input  (true/false)\n\
	def outcome = input message: '${1}', parameters: [\n\
	    [name: 'myFlag', description: 'My flag', \\$class: 'BooleanParameterDefinition']\n\
	]\n\
# input (text)\n\
snippet input  (text)\n\
	def outcome = input message: '${1}', parameters: [\n\
	    [name: 'myText', description: 'My text', \\$class: 'StringParameterDefinition']\n\
	]\n\
# input (choice)\n\
snippet input  (choice)\n\
	def outcome = input message: '${1:Please select}', parameters: [\n\
	    [name: 'myChoice', description: 'My choice', choices: 'Choice 1\\nChoice 2\\nChoice 3', \\$class: 'ChoiceParameterDefinition']\n\
	]\n\
#  ${env.BUILD_NUMBER} \n\
snippet  ${env.BUILD_NUMBER} \n\
	\\${env.BUILD_NUMBER}\n\
#  ${env.BUILD_ID} \n\
snippet  ${env.BUILD_ID} \n\
	\\${env.BUILD_ID}\n\
#  ${env.BUILD_DISPLAY_NAME} \n\
snippet  ${env.BUILD_DISPLAY_NAME} \n\
	\\${env.BUILD_DISPLAY_NAME}\n\
#  ${env.JOB_NAME} \n\
snippet  ${env.JOB_NAME} \n\
	\\${env.JOB_NAME}\n\
#  ${env.BUILD_TAG} \n\
snippet  ${env.BUILD_TAG} \n\
	\\${env.BUILD_TAG}\n\
#  ${env.EXECUTOR_NUMBER} \n\
snippet  ${env.EXECUTOR_NUMBER} \n\
	\\${env.EXECUTOR_NUMBER}\n\
#  ${env.NODE_NAME} \n\
snippet  ${env.NODE_NAME} \n\
	\\${env.NODE_NAME}\n\
#  ${env.NODE_LABELS} \n\
snippet  ${env.NODE_LABELS} \n\
	\\${env.NODE_LABELS}\n\
#  ${env.WORKSPACE} \n\
snippet  ${env.WORKSPACE} \n\
	\\${env.WORKSPACE}\n\
#  ${env.JENKINS_HOME} \n\
snippet  ${env.JENKINS_HOME} \n\
	\\${env.JENKINS_HOME}\n\
#  ${env.JENKINS_URL} \n\
snippet  ${env.JENKINS_URL} \n\
	\\${env.JENKINS_URL}\n\
#  ${env.BUILD_URL} \n\
snippet  ${env.BUILD_URL} \n\
	\\${env.BUILD_URL}\n\
#  ${env.JOB_URL} \n\
snippet  ${env.JOB_URL} \n\
	\\${env.JOB_URL}\n\
";

    exports.snippetText = groovySnippets + '\n' + workflowSnippets;
    exports.scope = "groovy";

    // Also see http://stackoverflow.com/questions/26089258/ace-editor-manually-adding-snippets

});
