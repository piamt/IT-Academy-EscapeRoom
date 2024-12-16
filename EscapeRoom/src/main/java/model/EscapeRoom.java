package model;

public class EscapeRoom {

    private Integer idEscaperoom;
    private String name;
    private String cif;

    public EscapeRoom(String name, String cif) {
        this.name = name;
        this.cif = cif;
    }

    public EscapeRoom() {}

    public String getName() {
        return name;
    }

    public void setIdEscaperoom(Integer idEscaperoom) {
        this.idEscaperoom = idEscaperoom;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCif() {
        return cif;
    }

    public void setCif(String cif) {
        this.cif = cif;
    }

    public Integer getIdEscaperoom() {
        return idEscaperoom;
    }

    @Override
    public String toString() {
        return "EscapeRoom{" +
                "idEscaperoom=" + idEscaperoom +
                ", name='" + name + '\'' +
                ", cif='" + cif + '\'' +
                '}';
    }
}
