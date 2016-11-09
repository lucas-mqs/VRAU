package br.com.sovrau.constants;

/**
 * Created by Lucas on 12/05/2016.
 */
public class Constants {
    public static final String FIREBASE_URL = "https://sovrau.firebaseio.com/";
    public static final String NODE_DATABASE = "database";
    public static final String NODE_MOTO = "moto";
    public static final String NODE_PERCURSO = "percurso";
    public static final String NODE_USER = "users";
    public static final String EXTRA_MOTO_EDITAR = "vrau.EDITAR_MOTO";
    public static final String EXTRA_MOTO_ADICIONADA = "vrau.MOTO_ADICIONADA";
    public static final String EXTRA_USUARIO_LOGADO = "vrau.USUARIO_LOGADO";
    public static final String EXTRA_PERCURSO_ADICIONADO = "vrau.PERCURSO_ADICIONADO";
    public static final String NODE_ALERTA = "alerta";
    public static final String LAT = "latitude";
    public static final String LNG = "longitude";
    //----------------------------------------------------------------------------------
    //------------------------------- CONSTANTES MOTO DTO ------------------------------

    public static final String MONITORAR_FREIOS = "isMonitorarFreios";
    public static final String ODOMETRO = "odometro";
    public static final String CILINDRADAS = "cilindradas";
    public static final String LOCAL_CELULAR = "localCelular";
    public static final String MONITORAR_COMBUSTIVEL = "isMonitorarCombustivel";
    public static final String MODELO = "modelo";
    public static final String MARCA = "marca";
    public static final String NOME = "nome";
    public static final String MONITORAR_OLEO = "isMonitorarOleo";
    public static final String ANO = "ano";
    public static final String MONITORAR_LIQUIDO = "isMonitorarLiquido";
    public static final String ID = "id";
    public static final String OBS = "obs";
    public static final String TANQUE = "tanque";
    public static final String MONITORAR_CX_DIRECAO = "isMonitorarCxDirecao";
    public static final String MONITORAR_PNEUS = "isMonitorarPneus";
    public static final String MONITORAR_PASTILHAS = "isMonitorarPastilhas";
    public static final String PLACA = "placa";

    //----------------------------------------------------------------------------------
    //---------------------------------- CONSTANSTES PERCURSO --------------------------

    public static final String MEDIR_AUTO = "isMedirAuto";
    public static final String ODOMETRO_INICIAL = "odometroInicial";
    public static final String FINAL_PERCURSO = "final";
    public static final String MOTIVO = "motivo";
    public static final String DETECTAR_FIM_PERCURSO = "isDetectarFimPercurso";
    public static final String INICIO_PERCURSO = "inicio";
    public static final String TIPO_PERCURSO = "tipoPercurso";

    //"tipoAlerta", "percentualAtual", "indicador", "avisoTroca"
    //----------------------------------------------------------------------------------
    //---------------------------------- CONSTANSTES ALERTAS ---------------------------

    public static final String TIPO_ALERTA = "tipoAlerta";
    public static final String PERCENTUAL_ATUAL = "percentualAtual";
    public static final String INDICADOR = "indicador";
    public static final String AVISO_TROCA = "avisoTroca";
    public static final String PERCENTUAL_AERTA = "percentualAlerta";
    public static final String KM_RODADOS = "qtdeKmRodado";
    public static final String KM_FALTANTES = "qtdeKmFalta";


}
