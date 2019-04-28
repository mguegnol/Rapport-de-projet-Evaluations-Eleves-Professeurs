package schoolg;

/*
    Reponse question 1 :
        La classe professeur et eleves ont comme proprietee commune nom et prenom.
        Il est donc utile de definir une classe NomPrenom qui possède les atributs et les methodes
        permettant le developpement de cette demande.
        Pour y faire face deux possiblitees s'offre à nous : l'heritage par le biais d'une classe ou l'implementation
        par le biais d'une interface.
        Comme le fonctionnement d'un nom et d'un prenom est le même que ce soit un professeur ou un eleve les methodes
        propre à ces atributs seront definies dans la classe NomPrenom. De ce fait il est plus judicieux de ce tourner
        vers de l'heritage.
 */

/**
 * Classe definissant la gestion du nom et du prenom.
 */
public class NomPrenom {
    private String nom;
    private String prenom;

    /**
     * Constructeur necessitant le prenom et le nom
     * @param prenom Prenom allant être definit.
     * @param nom Nom allant être definit.
     */
    public NomPrenom(String prenom, String nom) {
        this.nom = nom;
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    @Override
    public String toString() {
        return nom.toUpperCase() + " " + prenom;
    }
}
