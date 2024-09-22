-- Create a list of first names and last names
WITH first_names AS (
    SELECT 'James' AS name UNION ALL
    SELECT 'Mary' UNION ALL
    SELECT 'John' UNION ALL
    SELECT 'Patricia' UNION ALL
    SELECT 'Robert' UNION ALL
    SELECT 'Linda' UNION ALL
    SELECT 'Michael' UNION ALL
    SELECT 'Barbara' UNION ALL
    SELECT 'William' UNION ALL
    SELECT 'Elizabeth' UNION ALL
    SELECT 'David' UNION ALL
    SELECT 'Jennifer' UNION ALL
    SELECT 'Richard' UNION ALL
    SELECT 'Maria' UNION ALL
    SELECT 'Charles' UNION ALL
    SELECT 'Susan' UNION ALL
    SELECT 'Joseph' UNION ALL
    SELECT 'Margaret' UNION ALL
    SELECT 'Thomas' UNION ALL
    SELECT 'Dorothy' UNION ALL
    SELECT 'Daniel' UNION ALL
    SELECT 'Helen' UNION ALL
    SELECT 'Matthew' UNION ALL
    SELECT 'Sandra' UNION ALL
    SELECT 'Anthony' UNION ALL
    SELECT 'Ashley' UNION ALL
    SELECT 'Mark' UNION ALL
    SELECT 'Donna' UNION ALL
    SELECT 'Paul' UNION ALL
    SELECT 'Carol' UNION ALL
    SELECT 'Andrew' UNION ALL
    SELECT 'Ruth' UNION ALL
    SELECT 'Joshua' UNION ALL
    SELECT 'Shirley' UNION ALL
    SELECT 'Kenneth' UNION ALL
    SELECT 'Angela' UNION ALL
    SELECT 'Kevin' UNION ALL
    SELECT 'Melissa' UNION ALL
    SELECT 'Brian' UNION ALL
    SELECT 'Deborah' UNION ALL
    SELECT 'George' UNION ALL
    SELECT 'Stephanie' UNION ALL
    SELECT 'Edward' UNION ALL
    SELECT 'Rebecca' UNION ALL
    SELECT 'Ronald' UNION ALL
    SELECT 'Laura' UNION ALL
    SELECT 'Timothy' UNION ALL
    SELECT 'Helen' UNION ALL
    SELECT 'Jason' UNION ALL
    SELECT 'Alice' UNION ALL
    SELECT 'Jeffrey' UNION ALL
    SELECT 'Judith' UNION ALL
    SELECT 'Ryan' UNION ALL
    SELECT 'Jacqueline' UNION ALL
    SELECT 'Jacob' UNION ALL
    SELECT 'Frances' UNION ALL
    SELECT 'Gary' UNION ALL
    SELECT 'Martha' UNION ALL
    SELECT 'Nicholas' UNION ALL
    SELECT 'Teresa' UNION ALL
    SELECT 'Eric' UNION ALL
    SELECT 'Doris' UNION ALL
    SELECT 'Stephen' UNION ALL
    SELECT 'Gloria' UNION ALL
    SELECT 'Larry' UNION ALL
    SELECT 'Evelyn' UNION ALL
    SELECT 'Justin' UNION ALL
    SELECT 'Jean' UNION ALL
    SELECT 'Scott' UNION ALL
    SELECT 'Cheryl' UNION ALL
    SELECT 'Brandon' UNION ALL
    SELECT 'Mildred' UNION ALL
    SELECT 'Benjamin' UNION ALL
    SELECT 'Katherine' UNION ALL
    SELECT 'Adam' UNION ALL
    SELECT 'Samantha' UNION ALL
    SELECT 'Samuel' UNION ALL
    SELECT 'Janet' UNION ALL
    SELECT 'Alexander' UNION ALL
    SELECT 'Megan' UNION ALL
    SELECT 'Patrick' UNION ALL
    SELECT 'Carolyn' UNION ALL
    SELECT 'Jack' UNION ALL
    SELECT 'Hannah' UNION ALL
    SELECT 'Dennis' UNION ALL
    SELECT 'Christine' UNION ALL
    SELECT 'Jerry' UNION ALL
    SELECT 'Emma' UNION ALL
    SELECT 'Tyler' UNION ALL
    SELECT 'Lauren' UNION ALL
    SELECT 'Aaron' UNION ALL
    SELECT 'Alice' UNION ALL
    SELECT 'Henry' UNION ALL
    SELECT 'Julia' UNION ALL
    SELECT 'Douglas' UNION ALL
    SELECT 'Marie' UNION ALL
    SELECT 'Keith' UNION ALL
    SELECT 'Ruby' UNION ALL
    SELECT 'Walter' UNION ALL
    SELECT 'Rose'
),
last_names AS (
    SELECT 'Smith' AS name UNION ALL
    SELECT 'Johnson' UNION ALL
    SELECT 'Williams' UNION ALL
    SELECT 'Jones' UNION ALL
    SELECT 'Brown' UNION ALL
    SELECT 'Davis' UNION ALL
    SELECT 'Miller' UNION ALL
    SELECT 'Wilson' UNION ALL
    SELECT 'Moore' UNION ALL
    SELECT 'Taylor' UNION ALL
    SELECT 'Anderson' UNION ALL
    SELECT 'Thomas' UNION ALL
    SELECT 'Jackson' UNION ALL
    SELECT 'White' UNION ALL
    SELECT 'Harris' UNION ALL
    SELECT 'Martin' UNION ALL
    SELECT 'Thompson' UNION ALL
    SELECT 'Garcia' UNION ALL
    SELECT 'Martinez' UNION ALL
    SELECT 'Robinson' UNION ALL
    SELECT 'Clark' UNION ALL
    SELECT 'Rodriguez' UNION ALL
    SELECT 'Lewis' UNION ALL
    SELECT 'Lee' UNION ALL
    SELECT 'Walker' UNION ALL
    SELECT 'Hall' UNION ALL
    SELECT 'Allen' UNION ALL
    SELECT 'Young' UNION ALL
    SELECT 'King' UNION ALL
    SELECT 'Wright' UNION ALL
    SELECT 'Scott' UNION ALL
    SELECT 'Torres' UNION ALL
    SELECT 'Nguyen' UNION ALL
    SELECT 'Hill' UNION ALL
    SELECT 'Adams' UNION ALL
    SELECT 'Baker' UNION ALL
    SELECT 'Nelson' UNION ALL
    SELECT 'Carter' UNION ALL
    SELECT 'Mitchell' UNION ALL
    SELECT 'Perez' UNION ALL
    SELECT 'Roberts' UNION ALL
    SELECT 'Turner' UNION ALL
    SELECT 'Phillips' UNION ALL
    SELECT 'Campbell' UNION ALL
    SELECT 'Parker' UNION ALL
    SELECT 'Evans' UNION ALL
    SELECT 'Edwards' UNION ALL
    SELECT 'Collins' UNION ALL
    SELECT 'Stewart' UNION ALL
    SELECT 'Sanchez' UNION ALL
    SELECT 'Morris' UNION ALL
    SELECT 'Rogers' UNION ALL
    SELECT 'Reed' UNION ALL
    SELECT 'Cook' UNION ALL
    SELECT 'Morgan' UNION ALL
    SELECT 'Bell' UNION ALL
    SELECT 'Murphy' UNION ALL
    SELECT 'Bailey' UNION ALL
    SELECT 'Rivera' UNION ALL
    SELECT 'Cooper' UNION ALL
    SELECT 'Richardson' UNION ALL
    SELECT 'Cox' UNION ALL
    SELECT 'Howard' UNION ALL
    SELECT 'Ward' UNION ALL
    SELECT 'Torres' UNION ALL
    SELECT 'Peterson' UNION ALL
    SELECT 'Gray' UNION ALL
    SELECT 'Ramirez' UNION ALL
    SELECT 'James' UNION ALL
    SELECT 'Watson' UNION ALL
    SELECT 'Brooks' UNION ALL
    SELECT 'Kelly' UNION ALL
    SELECT 'Sanders' UNION ALL
    SELECT 'Price' UNION ALL
    SELECT 'Bennett' UNION ALL
    SELECT 'Wood' UNION ALL
    SELECT 'Barnes' UNION ALL
    SELECT 'Ross' UNION ALL
    SELECT 'Henderson' UNION ALL
    SELECT 'Coleman' UNION ALL
    SELECT 'Jenkins' UNION ALL
    SELECT 'Perry' UNION ALL
    SELECT 'Powell' UNION ALL
    SELECT 'Long' UNION ALL
    SELECT 'Patterson' UNION ALL
    SELECT 'Hughes' UNION ALL
    SELECT 'Flores' UNION ALL
    SELECT 'Washington' UNION ALL
    SELECT 'Butler' UNION ALL
    SELECT 'Simmons' UNION ALL
    SELECT 'Foster' UNION ALL
    SELECT 'Gonzalez' UNION ALL
    SELECT 'Bryant' UNION ALL
    SELECT 'Alexander' UNION ALL
    SELECT 'Russell' UNION ALL
    SELECT 'Griffin' UNION ALL
    SELECT 'Diaz' UNION ALL
    SELECT 'Hayes' UNION ALL
    SELECT 'Myers' UNION ALL
    SELECT 'Ford'
),
random_names AS (
    SELECT
        first_names.name AS first_name,
        last_names.name AS last_name
    FROM
        first_names
    CROSS JOIN
        last_names
    ORDER BY
        RAND()
    LIMIT 250
)
INSERT INTO vets (first_name, last_name)
SELECT first_name, last_name FROM random_names;




-- Add specialties for 80% of the vets
WITH vet_ids AS (
    SELECT id
    FROM vets
    ORDER BY RAND()
    LIMIT 200  -- 80% of 1000
),
specialties AS (
    SELECT id
    FROM specialties
),
random_specialties AS (
    SELECT 
        vet_ids.id AS vet_id,
        specialties.id AS specialty_id
    FROM 
        vet_ids
    CROSS JOIN 
        specialties
    ORDER BY 
        RAND()
    LIMIT 300  -- 2 specialties per vet on average
)
INSERT INTO vet_specialties (vet_id, specialty_id)
SELECT 
    vet_id,
    specialty_id
FROM (
    SELECT 
        vet_id,
        specialty_id,
        ROW_NUMBER() OVER (PARTITION BY vet_id ORDER BY RAND()) AS rn
    FROM 
        random_specialties
) tmp
WHERE 
    rn <= 2;  -- Assign at most 2 specialties per vet

-- The remaining 20% of vets will have no specialties, so no need for additional insertion commands
