# Requirements
- python > 3.9

# How to set up python environment
- run `python -m venv venv`
- run `source venv/bin/activate`
- run `pip install -r requirements.txt`

# How to set up mongo db
- run `docker-compose --profile=mongodb up`
- run `python migrate.py`
- connection string - `mongodb://petclinic:petclinic@localhost:27017/`
- username - `petclinic`
- password - `petclinic`
- db - `petclinic`
