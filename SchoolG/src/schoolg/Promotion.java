package schoolg;

import java.util.*;

/*
    Reponse question 5.
    TODO Repondre à la question 5
 */

/**
 * Classe decrivant une promotion. Elle contient un nom et une liste d'eleve qui la compose.
 */
public class Promotion {
    private String                      nom;
    private ArrayList<Eleve>            eleves = new ArrayList<>();

    /**
     * Constructeur
     * @param nom Nom de la promotion
     */
    public                              Promotion(String nom) {
        this.nom = nom;
    }

    public String                       getNom() {
        return nom;
    }

    public void                         setNom(String nom) {
        this.nom = nom;
    }

    public ArrayList<Eleve>             getEleves() {
        return eleves;
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();

        res.append("Nom de la promotion : ").append(nom).append("\n");
        for (Eleve eleve : eleves){
            res.append("\t").append(eleve.getUid()).append("-").append(eleve.getPrenom()).append(" ")
                    .append(eleve.getNom()).append("\t(m=").append(eleve.getMoyenne()).append("; M=").append(eleve.getMediane())
                    .append(")\n");
        }
        return res.toString();
    }

    /**
     * Ajoute un eleve a la promotion
     * @param eleve Eleve ajoute à la promotion.
     */
    public void                         setEleves(Eleve eleve) {
        eleves.add(eleve);
    }

    /**
     * Recherche un eleve dans la promotion concernee
     * @param uid Identifiant de l'eleve.
     * @return Elve trouve, null si il n'existe pas.
     */
    public Eleve                        rechercher(int uid) {
        for (Eleve actuel : eleves)
            if (actuel.getUid() == uid)
                return (actuel);
        return (null);
    }
    
    public void triUid() {
        eleves.sort(Eleve.UidComparator.getUidComparator());
    }

    public void triReverseUid() {
        eleves.sort(Eleve.UidReverseComparator.getUidReverseComparator());
    }

    public void triMoyenne() {
        eleves.sort(Eleve.MoyenneComparator.getMoyenneComparator());
    }

    public void triReverseMoyenne() {
        eleves.sort(Eleve.MoyenneReverseComparator.getMoyenneReverseComparator());
    }

    public void triMediane() {
        eleves.sort(Eleve.MedianeComparator.getMedianeComparator());
    }

    public void triReverseMediane() {
        eleves.sort(Eleve.MedianeReverseComparator.getMedianeComparator());
    }
}
