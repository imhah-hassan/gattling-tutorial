from faker import Faker
from faker import Factory
fakerFR = Factory.create('fr_FR')
faker_en = Faker()

print("nom;prenom;matricule;date_de_naissance")
for i in range(100):
    nom = fakerFR.last_name()
    prenom = fakerFR.first_name()
    matricule = str(fakerFR.random_number(5))
    date_de_naissance = str(fakerFR.date_of_birth(tzinfo=None, minimum_age=22, maximum_age=60))
    print( ";".join((nom, prenom, matricule, date_de_naissance)))