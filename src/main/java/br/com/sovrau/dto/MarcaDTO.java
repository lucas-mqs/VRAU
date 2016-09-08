package br.com.sovrau.dto;

/**
 * Created by Lucas on 08/05/2016.
 */
public class MarcaDTO {
    private long idMarca;
    private String nmMarca;

    public long getIdMarca() {
        return idMarca;
    }

    public void setIdMarca(long idMarca) {
        this.idMarca = idMarca;
    }

    public String getNmMarca() {
        return nmMarca;
    }

    public void setNmMarca(String nmMarca) {
        this.nmMarca = nmMarca;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MarcaDTO marcaDTO = (MarcaDTO) o;

        if (idMarca != marcaDTO.idMarca) return false;
        return nmMarca != null ? nmMarca.equals(marcaDTO.nmMarca) : marcaDTO.nmMarca == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (idMarca ^ (idMarca >>> 32));
        result = 31 * result + (nmMarca != null ? nmMarca.hashCode() : 0);
        return result;
    }
}
