package schoolg;

import java.text.DecimalFormat;
import java.util.*;

import static java.lang.Float.parseFloat;

/**
 * Classe representant un eleve.
 */
public class Eleve extends NomPrenom implements Comparable<Eleve> {

    private int                     uid;
    private float                   moyenne = 0;
    private float                   mediane = 0;
    private String                  promotion = "Non defini";

    public                          Eleve(String prenom, String nom, int uid) {
        super(prenom, nom);
        this.uid = uid;
    }

    /**
     * Constructeur d'Eleve
     * @param prenom Prenom de l'eleve cree
     * @param nom Nom de l'eleve cree
     * @param uid Identifiant unique de l'eleve cree
     * @param promotion Promotion de l'eleve cree, n'est pas obligatoire a la creation.
     */
    public                          Eleve(String prenom, String nom, int uid, Promotion promotion) {
        super(prenom, nom);
        this.uid = uid;
        this.promotion = promotion.getNom();
        promotion.setEleves(this);
    }

    public int                      getUid() {
        return uid;
    }

    /**
     * Fonction permettant de definir la mediane.
     * @param mediane Definition de la mediane, ne realise aucun calcul.
     */

    public void                    setMediane(float mediane) {
        this.mediane = mediane;
    }

    /**
     * Fonction permettant de definir la moyenne.
     * @param moyenne Definition de la mediane, ne realise aucun calcul.
     */
    public void                      setMoyenne(float moyenne) {
        this.moyenne = moyenne;
    }

    public float                    getMediane() {
        return formatFloat(mediane);
    }

    /**
     * Fonction permettant de formater un float en format 0.00 et de le repasser en float.
     * @param valeur Valeur non formatee
     * @return Valeur formatee au format 0.00
     */
    private float formatFloat(float valeur) {
        DecimalFormat dec = new DecimalFormat("#0.00");
        String tmp = dec.format(valeur);
        tmp = tmp.replace(',', '.');
        return parseFloat(tmp);
    }

    public float                    getMoyenne() {
        return formatFloat(moyenne);
    }

    public void                     setPromotion(String promotion) {
        this.promotion = promotion;
    }

    public void                     setPromotion(Promotion promotion) {
        this.promotion = promotion.getNom();
        promotion.setEleves(this);
    }

    /**
     * Fonction permettant d'avoir tous les correcteurs pour l'instance d'Eleve actuelle.
     * @return Set des professeurs ayant corrige l'elève.
     */
    public Set<Professeur>          getCorrecteurs() {
        Set<Professeur>             set_correcteurs = new HashSet<Professeur>();
        ArrayList<Evaluation>       liste_evaluations = Evaluation.getEvaluations(uid);

        for (Evaluation actuel : liste_evaluations) {
            set_correcteurs.add(actuel.getProfesseur_correcteur());
        }
        return (set_correcteurs);
    }

    @Override
    public String                   toString() {
        ArrayList<Evaluation> liste_evaluations = Evaluation.getEvaluations(uid);
        StringBuilder ret = new StringBuilder();
        ret.append(super.toString());
        ret.append("id:").append(uid).append("\n");
        ret.append("promotion: ").append(promotion);
        ret.append("\nnotes: ");
        for (Evaluation actuel : liste_evaluations) {
            ret.append(actuel.getMatiere()).append(" ");
            ret.append(actuel.getNote()).append(" ");
        }
        ret.append("\nmoyenne = ").append(getMoyenne());
        ret.append("\nmediane = ").append(getMediane()).append("\n");
        ret.append(getCorrecteurs());

        return (ret.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Eleve eleve = (Eleve) o;
        return getUid() == eleve.getUid() &&
                Float.compare(eleve.getMoyenne(), getMoyenne()) == 0 &&
                Float.compare(eleve.getMediane(), getMediane()) == 0;
    }

    @Override
    public int hashCode() {
        int result = 17;

        result = 31 * result + getUid();
        result = 31 * result + (int)getMoyenne();
        result = 31 * result + (int)getMediane();
        return result;
    }


    @Override
    public int compareTo(Eleve o) {
        return (this.uid - o.getUid());
    }

    public int compareToReverse(Eleve o) {
        return (o.getUid() - this.uid);
    }

    /**
     * Classe comparant par l'Uid.
     */
    static class UidComparator implements Comparator<Eleve> {

        public static UidComparator getUidComparator() {
            return new UidComparator();
        }

        @Override
        public int compare(Eleve o1, Eleve o2) {
            return o1.compareTo(o2);
        }

    }

    /**
     * Classe comparant par l'Uid en ordre inverse.
     */
    static class UidReverseComparator implements Comparator<Eleve> {

        public static UidReverseComparator getUidReverseComparator() {
            return new UidReverseComparator();
        }

        @Override
        public int compare(Eleve o1, Eleve o2) {
            return o1.compareToReverse(o2);
        }
    }

    /**
     * Classe comparant par la moyenne.
     */
    static class MoyenneComparator implements Comparator<Eleve> {

        public static MoyenneComparator getMoyenneComparator() {
            return new MoyenneComparator();
        }


        @Override
        public int compare(Eleve o1, Eleve o2) {
            return Float.compare(o1.getMoyenne(), o2.getMoyenne());
        }
    }

    /**
     * Classe comparant par la moyenne en ordre inverse.
     */
    static class MoyenneReverseComparator implements Comparator<Eleve> {

        public static MoyenneReverseComparator getMoyenneReverseComparator() {
            return new MoyenneReverseComparator();
        }

        @Override
        public int compare(Eleve o1, Eleve o2) {
            return Float.compare(o2.getMoyenne(), o1.getMoyenne());
        }
    }

    /**
     * Classe comarant par la mediane.
     */
    static class MedianeComparator implements Comparator<Eleve> {

        public static MedianeComparator getMedianeComparator() {
            return new MedianeComparator();
        }

        @Override
        public int compare(Eleve o1, Eleve o2) {
            return Float.compare(o1.getMediane(), o2.getMediane());
        }
    }

    /**
     * Classe comparant par la mediane inverse.
     */
    static class MedianeReverseComparator implements Comparator<Eleve> {

        public static MedianeReverseComparator getMedianeComparator() {
            return new MedianeReverseComparator();
        }

        @Override
        public int compare(Eleve o1, Eleve o2) {
            return Float.compare(o2.getMediane(), o1.getMediane());
        }
    }
 }
