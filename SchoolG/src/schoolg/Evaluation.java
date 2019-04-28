package schoolg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * Classe representant les evaluations
 */
public class Evaluation {
    /**
     * L'ArrayList contient toutes les evaluations crees depuis le debut de l'execution.
     */
    static private ArrayList<Evaluation>    evaluations = new ArrayList<Evaluation>();
    private String                          matiere;
    private float                           note;
    private Eleve                           eleve_corrige;
    private Professeur                      professeur_correcteur;

    /**
     * Unique constructeur de la classe.
     * @param matiere de l'evaluation concernee
     * @param note de l'evaluation concernee
     * @param eleve_corrige concerne par l'evaluation
     * @param professeur_correcteur concerne par l'evluation.
     */
    public                                  Evaluation(String matiere, float note, Eleve eleve_corrige, Professeur professeur_correcteur) {
        this.matiere = matiere;
        this.note = note;
        this.eleve_corrige = eleve_corrige;
        this.professeur_correcteur = professeur_correcteur;
        /*
          Ajout du nouvel element dans la liste static.
          */
        evaluations.add(this);
        calculMediane(eleve_corrige);
        calculMoyenne(eleve_corrige);
    }

    /**
     * Methode permettant le calcul de la mediane. Se sert de Eleve.setMediane pour definir la valeur dans l'instance.
     * @param eleve_corrige Eleve corrige concerne.
     * @throws IllegalStateException Erreur levee en cas de notes insufisantes.
     */
    public static void                             calculMediane(Eleve eleve_corrige) throws IllegalStateException {
        ArrayList<Evaluation>       liste_evaluations = Evaluation.getEvaluations(eleve_corrige.getUid());
        ArrayList<Float>            liste_note = new ArrayList<Float>();
        int                         middle = liste_evaluations.size()/2;

        if (liste_evaluations.size() == 0)
            throw new IllegalStateException("Nombre de notes insufisantes !");
        for (Evaluation actuel : liste_evaluations) {
            liste_note.add(actuel.getNote());
        }
        Collections.sort(liste_note);
        if (liste_note.size()%2 == 1) {
            eleve_corrige.setMediane(liste_note.get(middle));
        } else {
            eleve_corrige.setMediane((liste_note.get(middle - 1) + liste_note.get(middle)) / (float)2.0);
        }
    }

    /**
     * Methode permettant le calcul de la moyenne. Se sert de Eleve.setMoyenne pour deifinir la valeur de l'instance.
     * @param eleve Eleve corrige concerne.
     * @throws IllegalStateException Erreur levee en cas de notes insufisantes.
     */
    public static void                             calculMoyenne(Eleve eleve) throws IllegalStateException {
        ArrayList<Evaluation>       liste_evaluations = Evaluation.getEvaluations(eleve.getUid());
        float                       ret = 0;

        if (liste_evaluations.size() == 0)
            throw new IllegalStateException("Nombre de notes insufissantes !");
        for (Evaluation actuel : liste_evaluations) {
            ret += actuel.getNote();
        }
        eleve.setMoyenne(ret / liste_evaluations.size());
    }

    /**
     * Accesseur en lecture des evaluations.
     * @return Liste de toutes les evaluations depuis le debut de l'execution.
     */
    public static ArrayList<Evaluation>     getEvaluations() {
        return evaluations;
    }

    /**
     * Accesseur en lecture des evaluations selon l'Uid de l'etudiant reçu.
     * @param uid Etudiant concerne par les evaluations
     * @return Liste des evaluations liee a l'etudiant concerne.
     */
    public static ArrayList<Evaluation>     getEvaluations(int uid) {
        ArrayList<Evaluation>               ret = new ArrayList<Evaluation>();
        ArrayList<Evaluation>               liste_evaluations = Evaluation.getEvaluations();

        for (Evaluation actuel : liste_evaluations) {
            if (actuel.eleve_corrige.getUid() == uid)
                ret.add(actuel);
        }
        return (ret);
    }

    /**
     * Retourne la indiceeme evaluation pour l'etudiant Uid.
     * @param uid Eleve dont on desire la indiceeme note.
     * @param indice Indice de l'evaluation voulue.
     * @return Retourne le indicieeme element. Retourne null si inexistant.
     */
    public static Evaluation                getEvaluation(int uid, int indice) {
        ArrayList<Evaluation> liste_evaluations = getEvaluations(uid);

        if (indice > liste_evaluations.size())
            return (null);
        else
            return (liste_evaluations.get(indice));
    }
    
    /**
     * Delete a given evaluation from the static array
     * @param evaluation Given evaluation to delete
     */
    public static void 							delete_evaluation(Evaluation evaluation) {
    	Iterator<Evaluation> it = evaluations.iterator();
        while (it.hasNext()) {
        	Evaluation next_eval = it.next();
            if (evaluation == next_eval) {
                it.remove();
            }
        }
	}

    public float                            getNote() {
        return note;
    }

    public String                           getMatiere() {
        return matiere;
    }

    public Professeur                       getProfesseur_correcteur() {
        return professeur_correcteur;
    }

    public void 							setNote(float note) {
        this.note = note;
    }

    @Override
    public String                           toString() {
        return "(" + eleve_corrige.toString() + " "
                + professeur_correcteur + " "
                + matiere + " "
                + note + ")";
    }
}
