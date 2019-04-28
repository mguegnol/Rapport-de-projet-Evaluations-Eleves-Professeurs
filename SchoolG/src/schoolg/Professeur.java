package schoolg;

/**
 * Classe representant un professeur.
 */
public class Professeur extends NomPrenom {

    /**
     * Constructeur avec parametres.
     * @param prenom Prenom du professeur cree.
     * @param nom Nom du professeur cree
     */
    public              Professeur(String prenom, String nom) {
        super(prenom, nom);
    }

    /**
     * Fonction permettant la rechercher d'un eleve dans une promotion donnee.
     * @param uid Identifiant de l'eleve concerne.
     * @param promotion Promotion dans la quelle la recherche s'effectue.
     * @return Eleve.
     * */
    public Eleve        rechercher(int uid, Promotion promotion) {
        return (promotion.rechercher(uid));
    }

    /**
     * Fonction permettant de definir une note. Si l'eleve n'existe pas declare une exception.
     * Si la note existe dejà la modifie. Sinon la cree.
     * @param matiere Matière concerne par la note.
     * @param note Valeur de la note.
     * @param indice Index de la note pour l'eleve concerne.
     * @param uid Eleve concerne.
     * @param promotion Promotion de l'eleve concerne.
     * @throws IllegalStateException Declare une exception si l'eleve n'existe pas.
     */
  /*  public void       	editNote(String matiere, float note, int indice, int uid, Promotion promotion) throws IllegalStateException {
        Evaluation evaluation_eleve;

        if ((rechercher(uid, promotion)) == null)
            throw new IllegalStateException("L'eleve recherche n'existe pas.\n");
        else if ((evaluation_eleve = Evaluation.getEvaluation(uid, indice)) == null)
            evaluation_eleve.setNote(note);
        else
            new Evaluation(matiere, note, rechercher(uid, promotion), this);
    }*/
    
    public void       	setNote(String matiere, float note, int uid, Promotion promotion) throws IllegalStateException {
    	if (note > 20.0) {
    		throw new IllegalStateException("Une note ne peut etre superieure a 20.0\n");
    	}
        if ((rechercher(uid, promotion)) == null)
            throw new IllegalStateException("L'eleve recherche n'existe pas.\n");
        else
            new Evaluation(matiere, note, rechercher(uid, promotion), this);
    }
}
