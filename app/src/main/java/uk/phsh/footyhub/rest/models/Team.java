package uk.phsh.footyhub.rest.models;

/**
 * Model class for Team Json
 * @author Peter Blackburn
 */
public class Team {

    public int id;
    public String name;
    public String shortName;
    public String tla;
    public String crest;
    public String address;
    public int founded;
    public String venue;

    public Team() { }

    /**
     * @param id Team id as int
     * @param name Team name as String
     * @param shortName Team short name as String
     * @param tla Team code as String
     * @param crest Team logo as String
     */
    public Team(int id, String name, String shortName, String tla, String crest,
                String address, int founded, String venue) {
        this.id = id;
        this.name = name;
        this.shortName = shortName;
        this.tla = tla;
        this.crest = crest;
        this.address = address;
        this.founded = founded;
        this.venue = venue;
    }

}
