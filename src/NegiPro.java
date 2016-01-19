/**<< SPFILE����PSFILE���쐬����N���X >>**/
import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.text.*;
import java.math.BigDecimal;

public class NegiPro{
//     *<< �v���O�������s�O�Ɋm�F���ׂ����� >>  
    public static final String SPFILE_NAME = "test01.sp";
    public static final String PSFILE_NAME = "test01.ps";
    public static final String   PMOS_NAME = "CMOSP";
    public static final String   NMOS_NAME = "CMOSN";
    public static final String HEADER_FILE = "psHeader.lib";
    public static final String ELEMNT_FILE = "psElementList.lib";

    //< ���C������ >
    public static void main(String[] args){
        NegiPro ceps = new NegiPro();
        // checkFile():�����Ƃ��Ďw�肵��FILE�̑��݊m�F.
        //  -> ����:(<Str>File��)
        //  �� ���݂�'true'���Ԃ�.
        // pickElement():SPFILE���\�����Ă���f�q���(���,��)�𒊏o
        //  -> ����:(<Str>spFile��)
        // writeHeader():PSFILE��Header���쐬.
        //  -> ����:(<Str>headerFile��, <Str>psFile��)
        // writeElement():PSFILE�̑f�q�����쐬.
        //  -> ����:(<I[]>�f�q(���/��)���i�[�z��,<Str>�f�q���i�[File��, <Str>psFile��)
        //�g�p�f�q�i�[�z��(0:PMOS,1:NMOS,2:R,3:C)
        int[] useElement = new int[4];
        System.out.println("*--- < PROGRAM START > ---*");

        if( ceps.checkFile(SPFILE_NAME)==true ){
            //<< SPFILE����f�q���̒��o >>==================================
            useElement=ceps.pickElement(SPFILE_NAME);
            //for(int allIndex: useElement){System.out.println(allIndex);}

            //<< �f�q�������PSFILE�̍쐬 >>=================================
            //-----< Header�����̍쐬 >-------------------------------------
            if( ceps.checkFile(HEADER_FILE)==true ){
                ceps.writeHeader(HEADER_FILE,PSFILE_NAME);
            }else{System.out.println("["+HEADER_FILE+"] : NO.");}
            //-----< �f�q��񕔕��̍쐬 >-------------------------------------
            if( ceps.checkFile(ELEMNT_FILE)==true ){
                ceps.writeElement(useElement,ELEMNT_FILE,PSFILE_NAME);                
            }else{System.out.println("["+ELEMNT_FILE+"] : NO.");}    
            //-----< ���W��񕔕��̍쐬 >-------------------------------------
        }
        else{System.out.println("["+SPFILE_NAME+"] : NO.");}
        
        System.out.println("*--- < PROGRAM END > ---*");
    }
    
    //< SPFILE -> �g�p�f�q�̒��o >
    int[] pickElement(String inputFile){
        File objectFile = new File(inputFile);
        String line;
        //�g�p�f�q�i�[�z��(0:PMOS,1:NMOS,2:R,3:C)
        int[] useElement = new int[4];
        //=====< Inputfile����MOS�̋L�q�����o >=====
        //�� M�̑O�ɃX�y�[�X�������Ă���ƌ��o�s�\
        String strMOS = "^M";
        Pattern P_MOS = Pattern.compile(strMOS);
        //Inputfile����PMOS�̋L�q�����o
        Pattern P_CMOSP = Pattern.compile(PMOS_NAME);
        int useCMOSPBit=0;
        //Inputfile����NMOS�̋L�q�����o
        Pattern P_CMOSN = Pattern.compile(NMOS_NAME);
        int useCMOSNBit=0;
        //=====< Inputfile����RESISTER�̋L�q�����o >=====
        String strResistor = "^R";
        Pattern P_Resistor = Pattern.compile(strResistor);
        int useRBit=0;
        //=====< Inputfile����CAPACITOR�̋L�q�����o >=====
        String strCapacitor = "^C";
        Pattern P_Capacitor = Pattern.compile(strCapacitor);
        int useCBit=0;
        //********************************************
        try{
            BufferedReader br = new BufferedReader(new FileReader(inputFile));
            while((line = br.readLine()) != null){
                Matcher M_MOS = P_MOS.matcher(line);
                Matcher M_CMOSP = P_CMOSP.matcher(line);
                Matcher M_CMOSN = P_CMOSN.matcher(line);
                Matcher M_Resistor = P_Resistor.matcher(line);
                Matcher M_Capacitor = P_Capacitor.matcher(line);
                System.out.println("[SP] "+line);
                //-------------------------------------------------------------------
                //< MOS�̏�� >
                //*------------------------------------------------------------------
                if(M_MOS.find()){                
                    //< PMOS�̏�� >
                    if(M_CMOSP.find()){ System.out.println("->[MP] "+line); useCMOSPBit++; }
                    //< NMOS�̏�� >
                    if(M_CMOSN.find()){ System.out.println("->[MN] "+line); useCMOSNBit++; }
                }
                //-------------------------------------------------------------------
                //< R�̏�� >
                //*------------------------------------------------------------------
                if(M_Resistor.find()){  System.out.println("->[R] "+line); useRBit++; }
                //------------------------------------------------------------------*
                //-------------------------------------------------------------------
                //< C�̏�� >
                //*------------------------------------------------------------------
                if(M_Capacitor.find()){ System.out.println("->[C] "+line); useCBit++; }
                //------------------------------------------------------------------*
                //-------------------------------------------------------------------
                //< �f�q�����i�[ >
                //*------------------------------------------------------------------
                useElement[0]=useCMOSPBit; useElement[1]=useCMOSNBit;
                useElement[2]=useRBit;     useElement[3]=useCBit;
                //------------------------------------------------------------------*
            }
            br.close();
        }//END_try
        catch(Exception ex){
            ex.printStackTrace();
        }//END_catch
        return useElement;
    }//END_pickElement()
    //==================================================================*

    //===================================================================
    //< EPS��Header���L�q >
    //*==================================================================
    void writeHeader(String headerFile, String psFile){
        String line;
        Pattern P_start = Pattern.compile(".lib header");
        Pattern P_end   = Pattern.compile(".endl");
        boolean pickRange = false;
        boolean firstCall = true;
        List<String> pickHeaderList  = new ArrayList<String>();
        //********************************************
        try{
            //< psHeader.lib�������ǂݍ��� >
            BufferedReader br = new BufferedReader(new FileReader(headerFile));
            while((line = br.readLine()) != null){
                Matcher M_start = P_start.matcher(line);
                Matcher M_end   = P_end.matcher(line);
                if(M_start.find()){ pickRange=true; }
                if(M_end.find()){  pickRange=false; }
                if(pickRange==true){
                    //System.out.println("[line]"+line);
                    if(firstCall!=true){pickHeaderList.add(line);}
                    firstCall=false;
                }
            }//END_while
            br.close();

            //< ���o����psFile�ɏ����o�� >            
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(psFile)));
            for(int i=0; i<pickHeaderList.size(); i++){
                //System.out.println("  [list]"+pickHeaderList.get(i));
                pw.println(pickHeaderList.get(i));
            }
            pw.close();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }//END_writeHeader()

    //< EPS�̑f�q�\�������L�q >
    void writeElement(int[] useEle, String eleListFile, String psFile){
        String line;
        Pattern P_cmos   = Pattern.compile(".lib cmosPack");
        Pattern P_resist = Pattern.compile(".lib resistor");
        Pattern P_cap    = Pattern.compile(".lib capacitor");
        Pattern P_util    = Pattern.compile(".lib utility");
        Pattern P_endl   = Pattern.compile(".endl");
        boolean pickRange = false;
        boolean firstCall = true;
        int counter=0;
        List<String> eleInfoList = new ArrayList<String>();

        try{
            //< �ǂ̑f�q�����݂���̂������o >
            for(int index=0; index<useEle.length; index++){
                if(useEle[index]!=0){
                    // �� �z�����0�łȂ� -> ���̎�ނ����Ȃ��Ƃ�1�͑��݂���.
                    //System.out.println("[index]"+index);
                    //< MOS�����݂���ꍇ ���� �ŏ��ɌĂяo���ꂽ�ꍇ >
                    if( (index==0||index==1)&&firstCall==true ){
                        firstCall=false;
                        counter=0;
                        BufferedReader br = new BufferedReader(new FileReader(eleListFile));
                        while((line=br.readLine())!=null){
                            Matcher M_cmos = P_cmos.matcher(line);
                            Matcher M_endl = P_endl.matcher(line);
                            if(M_cmos.find()){ pickRange=true;  }
                            if(M_endl.find()){ pickRange=false; }
                            if( pickRange==true ){
                                //System.out.println("  [line]"+line);
                                if(counter!=0){eleInfoList.add(line);}
                                counter++;
                            }
                        }//END_while
                        pickRange=false;
                        br.close();
                    }
                    //< R�����݂���ꍇ >
                    if(index==2){
                        counter=0;
                        BufferedReader br = new BufferedReader(new FileReader(eleListFile));
                        while((line=br.readLine())!=null){
                            Matcher M_resist = P_resist.matcher(line);
                            Matcher M_endl = P_endl.matcher(line);
                            if(M_resist.find()){ pickRange=true;  }
                            if(M_endl.find()){   pickRange=false; }
                            if( pickRange==true ){
                                //System.out.println("  [line]"+line);
                                if(counter!=0){eleInfoList.add(line);}
                                counter++;
                            }
                        }//END_while
                        pickRange=false;
                        br.close();
                    }
                    //< C�����݂���ꍇ >
                    if(index==3){
                        pickRange=false;
                    }
                }//END_if(useEle[])
            }//END_for(index)

            //< dot��jumper�Ȃǂ̌����p�L�� >
            counter=0;
            BufferedReader br = new BufferedReader(new FileReader(eleListFile));
            while((line=br.readLine())!=null){
                Matcher M_util = P_util.matcher(line);
                Matcher M_endl = P_endl.matcher(line);
                if(M_util.find()){ pickRange=true;  }
                if(M_endl.find()){ pickRange=false; }
                if( pickRange==true ){
                    //System.out.println("  [line]"+line);
                    if(counter!=0){eleInfoList.add(line);}
                    counter++;
                }
            }//END_while
            br.close();

            //< ���o����psFile�ɏ����o�� >            
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(psFile,true)));
            for(int i=0; i<eleInfoList.size(); i++){
                //System.out.println("[list]"+eleInfoList.get(i));
                pw.println(eleInfoList.get(i));
            }
            pw.close();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    //< SPFILE�̗L�����m�F >
    boolean checkFile(String inputFile){
        File objectFile = new File(inputFile);
        boolean judge = false;
        if(objectFile.exists()){ judge = true; }
        //System.out.println(judge);
        return judge;
    }//END_checkSP()
}

// SimilarityVer13.java'���Q�l.
 
