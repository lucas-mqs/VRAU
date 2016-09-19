package br.com.sovrau.dto;

import java.io.Serializable;

/**
 * Created by Lucas on 18/09/2016.
 */
public class PercursoDTO implements Serializable {
    private String idPercurso;
    private String tipoPercurso;
    private String inicioPercurso;
    private String finalPercurso;
    private String odometroInicial;
    private String odometroFinal;
    private String obs;
    private boolean isAuto;
    private boolean isDetectarFim;

    public PercursoDTO() {
    }

    public PercursoDTO(String idPercurso, String tipoPercurso, String inicioPercurso, String finalPercurso, String odometroInicial, String odometroFinal, String obs, boolean isAuto, boolean isDetectarFim) {
        this.idPercurso = idPercurso;
        this.tipoPercurso = tipoPercurso;
        this.inicioPercurso = inicioPercurso;
        this.finalPercurso = finalPercurso;
        this.odometroInicial = odometroInicial;
        this.odometroFinal = odometroFinal;
        this.obs = obs;
        this.isAuto = isAuto;
        this.isDetectarFim = isDetectarFim;
    }

    public String getIdPercurso() {
        return idPercurso;
    }

    public void setIdPercurso(String idPercurso) {
        this.idPercurso = idPercurso;
    }

    public String getTipoPercurso() {
        return tipoPercurso;
    }

    public void setTipoPercurso(String tipoPercurso) {
        this.tipoPercurso = tipoPercurso;
    }

    public String getInicioPercurso() {
        return inicioPercurso;
    }

    public void setInicioPercurso(String inicioPercurso) {
        this.inicioPercurso = inicioPercurso;
    }

    public String getFinalPercurso() {
        return finalPercurso;
    }

    public void setFinalPercurso(String finalPercurso) {
        this.finalPercurso = finalPercurso;
    }

    public String getOdometroInicial() {
        return odometroInicial;
    }

    public void setOdometroInicial(String odometroInicial) {
        this.odometroInicial = odometroInicial;
    }

    public String getOdometroFinal() {
        return odometroFinal;
    }

    public void setOdometroFinal(String odometroFinal) {
        this.odometroFinal = odometroFinal;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public boolean isAuto() {
        return isAuto;
    }

    public void setAuto(boolean auto) {
        isAuto = auto;
    }

    public boolean isDetectarFim() {
        return isDetectarFim;
    }

    public void setDetectarFim(boolean detectarFim) {
        isDetectarFim = detectarFim;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PercursoDTO that = (PercursoDTO) o;

        if (isAuto != that.isAuto) return false;
        if (isDetectarFim != that.isDetectarFim) return false;
        if (idPercurso != null ? !idPercurso.equals(that.idPercurso) : that.idPercurso != null)
            return false;
        if (tipoPercurso != null ? !tipoPercurso.equals(that.tipoPercurso) : that.tipoPercurso != null)
            return false;
        if (inicioPercurso != null ? !inicioPercurso.equals(that.inicioPercurso) : that.inicioPercurso != null)
            return false;
        if (finalPercurso != null ? !finalPercurso.equals(that.finalPercurso) : that.finalPercurso != null)
            return false;
        if (odometroInicial != null ? !odometroInicial.equals(that.odometroInicial) : that.odometroInicial != null)
            return false;
        if (odometroFinal != null ? !odometroFinal.equals(that.odometroFinal) : that.odometroFinal != null)
            return false;
        return obs != null ? obs.equals(that.obs) : that.obs == null;

    }

    @Override
    public int hashCode() {
        int result = idPercurso != null ? idPercurso.hashCode() : 0;
        result = 31 * result + (tipoPercurso != null ? tipoPercurso.hashCode() : 0);
        result = 31 * result + (inicioPercurso != null ? inicioPercurso.hashCode() : 0);
        result = 31 * result + (finalPercurso != null ? finalPercurso.hashCode() : 0);
        result = 31 * result + (odometroInicial != null ? odometroInicial.hashCode() : 0);
        result = 31 * result + (odometroFinal != null ? odometroFinal.hashCode() : 0);
        result = 31 * result + (obs != null ? obs.hashCode() : 0);
        result = 31 * result + (isAuto ? 1 : 0);
        result = 31 * result + (isDetectarFim ? 1 : 0);
        return result;
    }
}
