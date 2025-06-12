**Routes Disponibles** : 

* /innovation/create
  * Liste d'idée
    * String person
    * String reference
    * int id enum des TYPES

* /pdf/generate
  * Liste d'idée
    * String title
    * String description
    * String imageUrl
    * String tag1 (Person)
    * String tag2 (Reference)
    * Enum au format STRING "MOVIE", etc

* /idea
  * /retrieve/all
    * Retourne une liste de BucketDTO
  * /retrive/{uid}
    * Retourne l'objet avec l'uid concerné
  * /add
    * Ajoute un objet en BDD (Pas nécessaire pour le front)
  * /delete/{uid}
    * Supprimes l'objet avec l'uid concerné
  * /retrieve/last/{numberOfElements}
    * Retourne les X derniers éléments crées en BDD
  * /retrieve/{page}/{size}
    * Retourne les X éléments de la page X

* /auth
  * /api/login 
    * Connecte l'user
  * /api/register
    * Enregistrer l'user
  * /me
    * Retourne les infos de l'user


**ERREUR GEREE**

/idea/delete/uid :
  Si idea non trouvée ==> Bad request "Idea not found"

/idea/retrieve/uid : 
  Si idea non trouvée ==> Bad request "Idea not found"

/auth/api/login :
  Si une erreur vis à vis de l'authent ==> Bad request "Invalid username or password"