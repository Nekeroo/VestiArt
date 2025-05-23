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

* /bucket
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