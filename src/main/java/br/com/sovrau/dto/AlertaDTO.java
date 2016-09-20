package br.com.sovrau.dto;

import java.io.Serializable;

public class AlertaDTO implements Serializable {
    private String idAlerta;
    private double porcentagemTotal;
    private double qtdeKmFalta;
    private double qtdeKmRodado;
    private double porcentagemAlerta;
    private int tipoAlerta;

    public AlertaDTO() {
    }

    public AlertaDTO(String idAlerta, double porcentagemTotal, double qtdeKmFalta, double qtdeKmRodado, double porcentagemAlerta, int tipoAlerta) {
        this.idAlerta = idAlerta;
        this.porcentagemTotal = porcentagemTotal;
        this.qtdeKmFalta = qtdeKmFalta;
        this.qtdeKmRodado = qtdeKmRodado;
        this.porcentagemAlerta = porcentagemAlerta;
        this.tipoAlerta = tipoAlerta;
    }

    public String getIdAlerta() {
        return idAlerta;
    }

    public void setIdAlerta(String idAlerta) {
        this.idAlerta = idAlerta;
    }

    public double getPorcentagemTotal() {
        return porcentagemTotal;
    }

    public void setPorcentagemTotal(double porcentagemTotal) {
        this.porcentagemTotal = porcentagemTotal;
    }

    public double getQtdeKmFalta() {
        return qtdeKmFalta;
    }

    public void setQtdeKmFalta(double qtdeKmFalta) {
        this.qtdeKmFalta = qtdeKmFalta;
    }

    public double getQtdeKmRodado() {
        return qtdeKmRodado;
    }

    public void setQtdeKmRodado(double qtdeKmRodado) {
        this.qtdeKmRodado = qtdeKmRodado;
    }

    public double getPorcentagemAlerta() {
        return porcentagemAlerta;
    }

    public void setPorcentagemAlerta(double porcentagemAlerta) {
        this.porcentagemAlerta = porcentagemAlerta;
    }

    public int getTipoAlerta() {
        return tipoAlerta;
    }

    public void setTipoAlerta(int tipoAlerta) {
        this.tipoAlerta = tipoAlerta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AlertaDTO alertaDTO = (AlertaDTO) o;

        if (Double.compare(alertaDTO.porcentagemTotal, porcentagemTotal) != 0) return false;
        if (Double.compare(alertaDTO.qtdeKmFalta, qtdeKmFalta) != 0) return false;
        if (Double.compare(alertaDTO.qtdeKmRodado, qtdeKmRodado) != 0) return false;
        if (Double.compare(alertaDTO.porcentagemAlerta, porcentagemAlerta) != 0) return false;
        if (tipoAlerta != alertaDTO.tipoAlerta) return false;
        return idAlerta != null ? idAlerta.equals(alertaDTO.idAlerta) : alertaDTO.idAlerta == null;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = idAlerta != null ? idAlerta.hashCode() : 0;
        temp = Double.doubleToLongBits(porcentagemTotal);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(qtdeKmFalta);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(qtdeKmRodado);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(porcentagemAlerta);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + tipoAlerta;
        return result;
    }
}
