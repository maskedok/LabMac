/**<< SPFILEからPSFILEを作成するクラス >>**/
import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.text.*;
import java.math.BigDecimal;

public class NegiPro{
//     *<< プログラム実行前に確認すべき項目 >>  
    public static final String SPFILE_NAME = "test01.sp";
    public static final String PSFILE_NAME = "test01.ps";
    public static final String   PMOS_NAME = "CMOSP";
    public static final String   NMOS_NAME = "CMOSN";
    public static final String HEADER_FILE = "psHeader.lib";
    public static final String ELEMNT_FILE = "psElementList.lib";

    //< メイン部分 >
    public static void main(String[] args){
        NegiPro ceps = new NegiPro();
        // checkFile():引数として指定したFILEの存在確認.
        //  -> 引数:(<Str>File名)
        //  ※ 存在で'true'が返る.
        // pickElement():SPFILEを構成している素子情報(種類,数)を抽出
        //  -> 引数:(<Str>spFile名)
        // writeHeader():PSFILEのHeaderを作成.
        //  -> 引数:(<Str>headerFile名, <Str>psFile名)
        // writeElement():PSFILEの素子情報を作成.
        //  -> 引数:(<I[]>素子(種類/数)情報格納配列,<Str>素子情報格納File名, <Str>psFile名)
        //使用素子格納配列(0:PMOS,1:NMOS,2:R,3:C)
        int[] useElement = new int[4];
        System.out.println("*--- < PROGRAM START > ---*");

        if( ceps.checkFile(SPFILE_NAME)==true ){
            //<< SPFILEから素子情報の抽出 >>==================================
            useElement=ceps.pickElement(SPFILE_NAME);
            //for(int allIndex: useElement){System.out.println(allIndex);}

            //<< 素子情報を基にPSFILEの作成 >>=================================
            //-----< Header部分の作成 >-------------------------------------
            if( ceps.checkFile(HEADER_FILE)==true ){
                ceps.writeHeader(HEADER_FILE,PSFILE_NAME);
            }else{System.out.println("["+HEADER_FILE+"] : NO.");}
            //-----< 素子情報部分の作成 >-------------------------------------
            if( ceps.checkFile(ELEMNT_FILE)==true ){
                ceps.writeElement(useElement,ELEMNT_FILE,PSFILE_NAME);                
            }else{System.out.println("["+ELEMNT_FILE+"] : NO.");}    
            //-----< 座標情報部分の作成 >-------------------------------------
        }
        else{System.out.println("["+SPFILE_NAME+"] : NO.");}
        
        System.out.println("*--- < PROGRAM END > ---*");
    }
    
    //< SPFILE -> 使用素子の抽出 >
    int[] pickElement(String inputFile){
        File objectFile = new File(inputFile);
        String line;
        //使用素子格納配列(0:PMOS,1:NMOS,2:R,3:C)
        int[] useElement = new int[4];
        //=====< InputfileからMOSの記述を検出 >=====
        //※ Mの前にスペースが入っていると検出不能
        String strMOS = "^M";
        Pattern P_MOS = Pattern.compile(strMOS);
        //InputfileからPMOSの記述を検出
        Pattern P_CMOSP = Pattern.compile(PMOS_NAME);
        int useCMOSPBit=0;
        //InputfileからNMOSの記述を検出
        Pattern P_CMOSN = Pattern.compile(NMOS_NAME);
        int useCMOSNBit=0;
        //=====< InputfileからRESISTERの記述を検出 >=====
        String strResistor = "^R";
        Pattern P_Resistor = Pattern.compile(strResistor);
        int useRBit=0;
        //=====< InputfileからCAPACITORの記述を検出 >=====
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
                //< MOSの情報 >
                //*------------------------------------------------------------------
                if(M_MOS.find()){                
                    //< PMOSの情報 >
                    if(M_CMOSP.find()){ System.out.println("->[MP] "+line); useCMOSPBit++; }
                    //< NMOSの情報 >
                    if(M_CMOSN.find()){ System.out.println("->[MN] "+line); useCMOSNBit++; }
                }
                //-------------------------------------------------------------------
                //< Rの情報 >
                //*------------------------------------------------------------------
                if(M_Resistor.find()){  System.out.println("->[R] "+line); useRBit++; }
                //------------------------------------------------------------------*
                //-------------------------------------------------------------------
                //< Cの情報 >
                //*------------------------------------------------------------------
                if(M_Capacitor.find()){ System.out.println("->[C] "+line); useCBit++; }
                //------------------------------------------------------------------*
                //-------------------------------------------------------------------
                //< 素子数を格納 >
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
    //< EPSのHeaderを記述 >
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
            //< psHeader.libから情報を読み込み >
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

            //< 抽出情報をpsFileに書き出し >            
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

    //< EPSの素子構成情報を記述 >
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
            //< どの素子が存在するのかを検出 >
            for(int index=0; index<useEle.length; index++){
                if(useEle[index]!=0){
                    // ※ 配列内が0でない -> その種類が少なくとも1つは存在する.
                    //System.out.println("[index]"+index);
                    //< MOSが存在する場合 かつ 最初に呼び出された場合 >
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
                    //< Rが存在する場合 >
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
                    //< Cが存在する場合 >
                    if(index==3){
                        pickRange=false;
                    }
                }//END_if(useEle[])
            }//END_for(index)

            //< dotやjumperなどの結線用記号 >
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

            //< 抽出情報をpsFileに書き出し >            
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
    
    //< SPFILEの有無を確認 >
    boolean checkFile(String inputFile){
        File objectFile = new File(inputFile);
        boolean judge = false;
        if(objectFile.exists()){ judge = true; }
        //System.out.println(judge);
        return judge;
    }//END_checkSP()
}

// SimilarityVer13.java'を参考.
 
