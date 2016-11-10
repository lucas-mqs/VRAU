package br.com.sovrau.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lucas on 03/05/2016.
 */
public class MotoDTO implements Serializable{
    private String idMoto;
    private long idMarca;
    private String nmMarca;
    private String idUsuario;
    private String nmMoto;
    private int cilindradasMoto;
    private String nmModelo;
    private int tanque;
    private int anoFabricacao;
    private String placa;
    private String obs;
    private long odometro;
    private List<PercursoDTO> listPercurso = new ArrayList<>();
    private List<AlertaDTO> listAlerta = new ArrayList<>();

    public MotoDTO() {
    }

    public MotoDTO(String idMoto, long idMarca, String nmMarca, String idUsuario, String nmMoto, int cilindradasMoto, String nmModelo, int tanque, int anoFabricacao, String placa, String obs, long odometro, List listPercurso, List listAlerta) {
        this.idMoto = idMoto;
        this.idMarca = idMarca;
        this.nmMarca = nmMarca;
        this.idUsuario = idUsuario;
        this.nmMoto = nmMoto;
        this.cilindradasMoto = cilindradasMoto;
        this.nmModelo = nmModelo;
        this.tanque = tanque;
        this.anoFabricacao = anoFabricacao;
        this.placa = placa;
        this.obs = obs;
        this.odometro = odometro;
        this.listPercurso = listPercurso;
        this.listAlerta = listAlerta;
    }

    public String getIdMoto() {
        return idMoto;
    }

    public void setIdMoto(String idMoto) {
        this.idMoto = idMoto;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

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

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNmMoto() {
        return nmMoto;
    }

    public void setNmMoto(String nmMoto) {
        this.nmMoto = nmMoto;
    }

    public int getCilindradasMoto() {
        return cilindradasMoto;
    }

    public void setCilindradasMoto(int cilindradasMoto) {
        this.cilindradasMoto = cilindradasMoto;
    }

    public String getNmModelo() {
        return nmModelo;
    }

    public void setNmModelo(String nmModelo) {
        this.nmModelo = nmModelo;
    }

    public int getTanque() {
        return tanque;
    }

    public void setTanque(int tanque) {
        this.tanque = tanque;
    }

    public int getAnoFabricacao() {
        return anoFabricacao;
    }

    public void setAnoFabricacao(int anoFabricacao) {
        this.anoFabricacao = anoFabricacao;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public long getOdometro() {
        return odometro;
    }

    public void setOdometro(long odometro) {
        this.odometro = odometro;
    }

    public List<AlertaDTO> getListAlerta() {
        return listAlerta;
    }

    public void setListAlerta(List<AlertaDTO> listAlerta) {
        this.listAlerta = listAlerta;
    }

    public List<PercursoDTO> getListPercurso() {
        return listPercurso;
    }

    public void setListPercurso(List<PercursoDTO> listPercurso) {
        this.listPercurso = listPercurso;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MotoDTO motoDTO = (MotoDTO) o;

        if (idMarca != motoDTO.idMarca) return false;
        if (cilindradasMoto != motoDTO.cilindradasMoto) return false;
        if (tanque != motoDTO.tanque) return false;
        if (anoFabricacao != motoDTO.anoFabricacao) return false;
        if (odometro != motoDTO.odometro) return false;
        if (idMoto != null ? !idMoto.equals(motoDTO.idMoto) : motoDTO.idMoto != null) return false;
        if (nmMarca != null ? !nmMarca.equals(motoDTO.nmMarca) : motoDTO.nmMarca != null)
            return false;
        if (idUsuario != null ? !idUsuario.equals(motoDTO.idUsuario) : motoDTO.idUsuario != null)
            return false;
        if (nmMoto != null ? !nmMoto.equals(motoDTO.nmMoto) : motoDTO.nmMoto != null) return false;
        if (nmModelo != null ? !nmModelo.equals(motoDTO.nmModelo) : motoDTO.nmModelo != null)
            return false;
        if (placa != null ? !placa.equals(motoDTO.placa) : motoDTO.placa != null) return false;
        if (obs != null ? !obs.equals(motoDTO.obs) : motoDTO.obs != null) return false;
        if (listPercurso != null ? !listPercurso.equals(motoDTO.listPercurso) : motoDTO.listPercurso != null)
            return false;
        return listAlerta != null ? listAlerta.equals(motoDTO.listAlerta) : motoDTO.listAlerta == null;

    }

    @Override
    public int hashCode() {
        int result = idMoto != null ? idMoto.hashCode() : 0;
        result = 31 * result + (int) (idMarca ^ (idMarca >>> 32));
        result = 31 * result + (nmMarca != null ? nmMarca.hashCode() : 0);
        result = 31 * result + (idUsuario != null ? idUsuario.hashCode() : 0);
        result = 31 * result + (nmMoto != null ? nmMoto.hashCode() : 0);
        result = 31 * result + cilindradasMoto;
        result = 31 * result + (nmModelo != null ? nmModelo.hashCode() : 0);
        result = 31 * result + tanque;
        result = 31 * result + anoFabricacao;
        result = 31 * result + (placa != null ? placa.hashCode() : 0);
        result = 31 * result + (obs != null ? obs.hashCode() : 0);
        result = 31 * result + (int) (odometro ^ (odometro >>> 32));
        result = 31 * result + (listPercurso != null ? listPercurso.hashCode() : 0);
        result = 31 * result + (listAlerta != null ? listAlerta.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "MotoDTO{" +
                "idMoto='" + idMoto + '\'' +
                ", idMarca=" + idMarca +
                ", nmMarca='" + nmMarca + '\'' +
                ", idUsuario='" + idUsuario + '\'' +
                ", nmMoto='" + nmMoto + '\'' +
                ", cilindradasMoto=" + cilindradasMoto +
                ", nmModelo='" + nmModelo + '\'' +
                ", tanque=" + tanque +
                ", anoFabricacao=" + anoFabricacao +
                ", placa='" + placa + '\'' +
                ", obs='" + obs + '\'' +
                ", odometro=" + odometro +
                '}';
    }
}
