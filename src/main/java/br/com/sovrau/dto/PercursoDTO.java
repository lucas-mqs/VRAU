package br.com.sovrau.dto;

/**
 * Created by Lucas.Marques on 07/11/2016.
 */
public class PercursoDTO {
    private boolean isMedirAuto;
    private long odometroInicial;
    private String enderecoFinal;
    private String motivo;
    private String id;
    private boolean isDetectarFimPercurso;
    private String enderecoInicial;
    private String tipoPercurso;

    public PercursoDTO(){

    }
    public PercursoDTO(boolean isMedirAuto, long odometroInicial, String enderecoFinal, String motivo, String id, boolean isDetectarFimPercurso, String enderecoInicial, String tipoPercurso) {
        this.isMedirAuto = isMedirAuto;
        this.odometroInicial = odometroInicial;
        this.enderecoFinal = enderecoFinal;
        this.motivo = motivo;
        this.id = id;
        this.isDetectarFimPercurso = isDetectarFimPercurso;
        this.enderecoInicial = enderecoInicial;
        this.tipoPercurso = tipoPercurso;
    }

    public boolean isMedirAuto() {
        return isMedirAuto;
    }

    public void setMedirAuto(boolean medirAuto) {
        isMedirAuto = medirAuto;
    }

    public long getOdometroInicial() {
        return odometroInicial;
    }

    public void setOdometroInicial(long odometroInicial) {
        this.odometroInicial = odometroInicial;
    }

    public String getEnderecoFinal() {
        return enderecoFinal;
    }

    public void setEnderecoFinal(String enderecoFinal) {
        this.enderecoFinal = enderecoFinal;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isDetectarFimPercurso() {
        return isDetectarFimPercurso;
    }

    public void setDetectarFimPercurso(boolean detectarFimPercurso) {
        isDetectarFimPercurso = detectarFimPercurso;
    }

    public String getEnderecoInicial() {
        return enderecoInicial;
    }

    public void setEnderecoInicial(String enderecoInicial) {
        this.enderecoInicial = enderecoInicial;
    }

    public String getTipoPercurso() {
        return tipoPercurso;
    }

    public void setTipoPercurso(String tipoPercurso) {
        this.tipoPercurso = tipoPercurso;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PercursoDTO that = (PercursoDTO) o;

        if (isMedirAuto != that.isMedirAuto) return false;
        if (odometroInicial != that.odometroInicial) return false;
        if (isDetectarFimPercurso != that.isDetectarFimPercurso) return false;
        if (enderecoFinal != null ? !enderecoFinal.equals(that.enderecoFinal) : that.enderecoFinal != null)
            return false;
        if (motivo != null ? !motivo.equals(that.motivo) : that.motivo != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (enderecoInicial != null ? !enderecoInicial.equals(that.enderecoInicial) : that.enderecoInicial != null)
            return false;
        return tipoPercurso != null ? tipoPercurso.equals(that.tipoPercurso) : that.tipoPercurso == null;

    }

    @Override
    public int hashCode() {
        int result = (isMedirAuto ? 1 : 0);
        result = 31 * result + (int) (odometroInicial ^ (odometroInicial >>> 32));
        result = 31 * result + (enderecoFinal != null ? enderecoFinal.hashCode() : 0);
        result = 31 * result + (motivo != null ? motivo.hashCode() : 0);
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (isDetectarFimPercurso ? 1 : 0);
        result = 31 * result + (enderecoInicial != null ? enderecoInicial.hashCode() : 0);
        result = 31 * result + (tipoPercurso != null ? tipoPercurso.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PercursoDTO{" +
                "isMedirAuto=" + isMedirAuto +
                ", odometroInicial=" + odometroInicial +
                ", enderecoFinal='" + enderecoFinal + '\'' +
                ", motivo='" + motivo + '\'' +
                ", id='" + id + '\'' +
                ", isDetectarFimPercurso=" + isDetectarFimPercurso +
                ", enderecoInicial='" + enderecoInicial + '\'' +
                ", tipoPercurso='" + tipoPercurso + '\'' +
                '}';
    }
}
