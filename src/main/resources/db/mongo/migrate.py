from pymongo import MongoClient
from dataclasses import dataclass
from datetime import date, datetime
from uuid import UUID, uuid4
import csv
from typing import Optional


FILE_PATHS = {
    "vets": "csv-files/vets.csv",
    "specs": "csv-files/specs.csv",
    "vets_specs": "csv-files/vets_specs.csv",
    "types": "csv-files/types.csv",
    "owners": "csv-files/owners.csv",
    "pets": "csv-files/pets.csv",
    "visits": "csv-files/visits.csv"
}


@dataclass
class VetRow:
    id: str
    first_name: str
    last_name: str


@dataclass
class Vet:
    first_name: str
    last_name: str
    speciality: Optional[str]

    def to_dict(self) -> dict:
        return {
            "first_name": self.first_name,
            "last_name": self.last_name,
            "speciality": self.speciality
        }


@dataclass
class VisitRow:
    id: str
    pet_id: str
    visit_date: str
    description: str


@dataclass
class Visit:
    visit_date: date
    description: str

    def to_dict(self) -> dict:
        return {
            "visit_date": self.visit_date,
            "description": self.description
        }


@dataclass
class PetRow:
    id: str
    name: str
    birth_date: str
    type_id: str
    owner_id: str


@dataclass
class Pet:
    name: str
    birth_date: date
    type: str
    visits: list[Visit]

    def to_dict(self) -> dict:
        return {
            "name": self.name,
            "birth_date": self.birth_date,
            "type": self.type,
            "visits": [visit.to_dict() for visit in self.visits]
        }


@dataclass
class OwnerRow:
    id: str
    first_name: str
    last_name: str
    address: str
    city: str
    telephone: str


@dataclass
class Owner:
    first_name: str
    last_name: str
    address: str
    city: str
    telephone: str
    pets: dict[UUID: Pet]

    def to_dict(self) -> dict:
        return {
            "first_name": self.first_name,
            "last_name": self.last_name,
            "address": self.address,
            "city": self.city,
            "telephone": self.telephone,
            "pets": {pet_id: pet.to_dict() for pet_id, pet in self.pets.items()}
        }


def main() -> None:
    with open(FILE_PATHS["types"], newline='') as types_file:
        reader = csv.reader(types_file, delimiter=';')
        next(reader, None)
        types_map = {int(row[0]): row[1] for row in reader}

    with open(FILE_PATHS["specs"], newline='') as specs_file:
        reader = csv.reader(specs_file, delimiter=';')
        next(reader, None)
        specs_map = {int(row[0]): row[1] for row in reader}

    with open(FILE_PATHS["vets_specs"], newline='') as vet_specs_file:
        reader = csv.reader(vet_specs_file, delimiter=';')
        next(reader, None)
        vet_specs_map = {int(row[0]): int(row[1]) for row in reader}

    with open(FILE_PATHS["vets"], newline='') as vet_file:
        reader = csv.reader(vet_file, delimiter=';')
        next(reader, None)
        vet_rows = [VetRow(*row) for row in reader]

    with open(FILE_PATHS["visits"], newline='') as visits_file:
        reader = csv.reader(visits_file, delimiter=';')
        next(reader, None)
        visits_rows = [VisitRow(*row) for row in reader]

    with open(FILE_PATHS["pets"], newline='') as pets_file:
        reader = csv.reader(pets_file, delimiter=';')
        next(reader, None)
        pet_rows = [PetRow(*row) for row in reader]

    with open(FILE_PATHS["owners"], newline='') as owners_file:
        reader = csv.reader(owners_file, delimiter=';')
        next(reader, None)
        owner_rows = [OwnerRow(*row) for row in reader]

    vets = [
        Vet(
            first_name=row.first_name,
            last_name=row.last_name,
            speciality=specs_map.get(vet_specs_map.get(int(row.id), -1))
        ) for row in vet_rows
    ]

    owners = [
        Owner(
            first_name=row.first_name,
            last_name=row.last_name,
            address=row.address,
            city=row.city,
            telephone=row.telephone,
            pets={
                str(uuid4()): Pet(
                    name=pet_row.name,
                    birth_date=datetime.strptime(pet_row.birth_date, '%Y-%m-%d'),
                    type=types_map[int(pet_row.type_id)],
                    visits=[
                        Visit(
                            visit_date=datetime.strptime(visit_row.visit_date, '%Y-%m-%d'),
                            description=visit_row.description
                        ) for visit_row in visits_rows
                    ]
                ) for pet_row in pet_rows
            }
        ) for row in owner_rows
    ]

    client = MongoClient('mongodb://petclinic:petclinic@localhost:27017/')
    db = client['petclinic']

    vets_collection = db['vets']
    owners_collection = db['owners']
    specs_collection = db["specializations"]
    types_collection = db["types"]

    vets_collection.insert_many([vet.to_dict() for vet in vets])
    owners_collection.insert_many([owner.to_dict() for owner in owners])
    specs_collection.insert_many([{"name": name} for name in specs_map.values()])
    types_collection.insert_many([{"name": name} for name in types_map.values()])

    print("Migration done")


if __name__ == "__main__":
    main()
