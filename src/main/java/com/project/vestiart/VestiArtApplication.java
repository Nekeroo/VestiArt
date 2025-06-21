package com.project.vestiart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VestiArtApplication {

    public static void main(String[] args) {
        SpringApplication.run(VestiArtApplication.class, args);
    }

    // TODO : User - Récupérer ses propres créations --> In progess (Besoin d'associer l'USER dans le modèle Idea)
    // TODO : User - Ajouter un système de like (PS : Ajouter une table de jointure id_user, id_idea)
    // TODO : Admin - éditer un user
    // TODO : Admin - Avoir plus de contrôles sur les créations
    // TODO : Admin - Possibilité de HIDE une Idea (eventuellement directement la supprimer ? )
    // TODO : Admin - Lister les utilisateurs
    // TODO : Admin - Ban user


}
