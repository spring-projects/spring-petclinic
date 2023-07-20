import os as installer
try:
	import psycopg2
	
except ImportError:
	installer.system("pip install psycopg2 --user")
	print("All packages successfully installed")
	
	import psycopg2
 
 
conn = psycopg2.connect(
	database="postgres", user="postgres", password="admin", host="127.0.0.1", port="5432"
)

conn.autocommit = True

cursor = conn.cursor()

sql = '''CREATE database petclinic'''

cursor.execute(sql)

print("Database created successfully")

conn.close()
 