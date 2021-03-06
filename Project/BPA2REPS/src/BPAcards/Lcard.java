/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BPAcards;

/**
 *
 * @author zfd
 */
public class Lcard {
	protected static final int format1[] = new int[]{ 0, 1, 2, 3, 6, 14, 18, 19, 27, 31, 33, 37, 38, 44, 50, 56, 62, 66, 74, 75, 77, 78 };
	protected static final  int format2[] = new int[]{ 1, 1, 1, 3, 8, 4, 1, 8, 4, 1, 4, 1, 6, 6, 6, 6, 4, 8, 1, 2, 1, 2 };
	protected int bus_i=-1; 
	protected int bus_j=-1;			//节点编号
	public char  type='L';			//1 A1
	public char  subtype=' ';		//2 A1
	public char  chgcde=' ';		//3 A1
	public String  owner="   ";		//4-6 A3
	public String  name1="        ";		//7-14 A8
	public double  kV1=0;				//15-18 F4.0
	public int  meter=0;			//19 I1
	public String  name2="        ";		//20-27 A8
	public double  kV2=0;				//28-31 F4.0 
	public int  CktID=1;			//32 A1 回路号
	public double  IRatA=0;				//34-37 F4.0
	public int  OfCKT=0;						//38 I1 并联线路数目
	public double  R=0;				//39-44 F6.5
	public double  X=0;			//45-50 F6.5
	public double  G=0;					//51-56 F6.5 G/2
	public double  B=0;					//57-62 F6.5 B/2
	public double  miles=0;					//63-66 F4.1
	public String  DescData="       ";					//67-74 A8  线路说明数据
	public char  MonthIn=' ';					//75 A1  投运日期
	public int  YearIn=0;					//76-77 A2
	public char  MonthOut=' ';					//78 A1
	public int  YearOut=0;					//79-80 I2
	
	
	public String sqlInsert(String dbname,String tablename){
		//attention: escape for single quote marker as ''
		String sql="insert into "+dbname+"."+tablename+" values('"+type+"','"+chgcde+"','"+
				owner.replaceAll("'", "''")+"','"+name1.replaceAll("'", "''")+"','"+kV1+"','"+meter+"','"+
				name2.replaceAll("'", "''")+"','"+kV2+"','"+CktID+"','"+IRatA+"','"+
				R+"','"+X+"','"+G+"','"+B+"','"+miles+"','"+DescData+"',"+SelfFunc.bpadate(MonthIn,YearIn)+","+SelfFunc.bpadate(MonthOut, YearOut)+")";
		return sql;
	}
	public void CharToCard(String s) throws BPAtrsException
	{
		int len = SelfFunc.strlen(s);
		String temp;
		//char type;			//1 A1
		if (len >format1[0])
		{
			type = s.charAt(0);
		}
		else
		{
			return;
		}
		if (len >format1[1])
		{
			subtype = s.charAt(1);
		}
		else
		{
			return;
		}
		//char chgcde;		//3 A1
		if (len > format1[2])
		{
			chgcde = s.charAt(2);
		}
		else
		{
			return;
		}

		//string owner;		//4-6 A3
		if (len > format1[3])
		{
			owner = SelfFunc.substring(s, format1[3], format1[3] + SelfFunc.min(format2[3], len - format1[3]));
		}
		else
		{
			return;
		}
		//string name1;		//7-14 A8
		if (len >format1[4])
		{
			name1 = SelfFunc.substring(s, format1[4], format1[4] + SelfFunc.min(format2[4], len - format1[4]));
		}
		else
		{
			return;
		}
		//double kV1;				//15-18 F4.0
		if (len > format1[5])
		{
			temp = SelfFunc.substring(s, format1[5], format1[5] + SelfFunc.min(format2[5], len - format1[5]));
			kV1 = SelfFunc.strToD(temp);
		}
		else
		{
			return;
		}
		//int meter;			//19 I1
		if (len > format1[6])
		{
			temp = SelfFunc.substring(s, format1[6], format1[6] + SelfFunc.min(format2[6], len - format1[6]));
			meter=SelfFunc.strToI(temp);
		}
		else
		{
			return;
		}
		//string name2;		//20-27 A8
		if (len > format1[7])
		{
			name2 = SelfFunc.substring(s, format1[7], format1[7] + SelfFunc.min(format2[7], len - format1[7]));
		}
		else
		{
			return;
		}
		//double kV2;				//28-31 F4.0 
		if (len > format1[8])
		{
			temp = SelfFunc.substring(s, format1[8], format1[8] + SelfFunc.min(format2[8], len - format1[8]));
			kV2 = SelfFunc.strToD(temp);
		}
		else
		{
			return;
		}
		//char CktID;			//32 I1 回路号
		if (len > format1[9])
		{
			if(SelfFunc.charAt(s,format1[9])==' ')
			{
				CktID=1;
			}
			else{
				CktID = SelfFunc.charAt(s,format1[9])-48;
			}
			//attention： Chinese Characters
		}
		else
		{
			return;
		}
		//double IRatA;				//34-37 F4.0
		if (len > format1[10])
		{
			temp = SelfFunc.substring(s, format1[10], format1[10] + SelfFunc.min(format2[10], len - format1[10]));
			IRatA = SelfFunc.strToD(temp);
		}
		else
		{
			return;
		}
		//int OfCKT;						//38 I1 并联线路数目
		if (len > format1[11])
		{
			temp = SelfFunc.substring(s, format1[11], format1[11] + SelfFunc.min(format2[11], len - format1[11]));
			OfCKT = SelfFunc.strToI(temp);
		}
		else
		{
			return;
		}
		//double R;				//39-44 F6.5
		if (len > format1[12])
		{
			temp = SelfFunc.substring(s, format1[12], format1[12] + SelfFunc.min(format2[12], len - format1[12]));
			R = SelfFunc.strToD(temp,6,5);
		}
		else
		{
			return;
		}
		//double X;			//45-50 F6.5
		if (len > format1[13])
		{
			temp = SelfFunc.substring(s, format1[13], format1[13] + SelfFunc.min(format2[13], len - format1[13]));
			X = SelfFunc.strToD(temp,6,5);
		}
		else
		{
			return;
		}
		//double G;					//51-56 F6.5 G/2
		if (len > format1[14])
		{
			temp = SelfFunc.substring(s, format1[14], format1[14] + SelfFunc.min(format2[14], len - format1[14]));
			if((temp.length()==4)){
				System.out.println("dd");
			}
			G = SelfFunc.strToD(temp,6,5);
		}
		else
		{
			return;
		}
		//double B;					//57-62 F6.5 B/2
		if (len > format1[15])
		{
			temp = SelfFunc.substring(s, format1[15], format1[15] + SelfFunc.min(format2[15], len - format1[15]));
			B = SelfFunc.strToD(temp,6,5);
		}
		else
		{
			return;
		}
		//double miles;					//63-66 F4.1
		if (len > format1[16])
		{
			temp = SelfFunc.substring(s, format1[16], format1[16] + SelfFunc.min(format2[16], len - format1[16]));
			miles = SelfFunc.strToD(temp,4,1);
		}
		else
		{
			return;
		}
		//string DescData;					//67-74 A8  线路说明数据
		if (len > format1[17])
		{
			DescData= SelfFunc.substring(s, format1[17], format1[17] + SelfFunc.min(format2[17], len - format1[17]));
		}
		else
		{
			return;
		}
		//char MonthIn;					//75 A1  投运日期
		if (len > format1[18])
		{
			MonthIn = SelfFunc.charAt(s,format1[18]);
		}
		else
		{
			return;
		}
		//int YeatIn;					//76-77 A2
		if (len > format1[19])
		{
			temp = SelfFunc.substring(s, format1[19], format1[19] + SelfFunc.min(format2[19], len - format1[19]));
			YearIn = SelfFunc.strToI(temp);
		}
		else
		{
			return;
		}
		//char MonthOut;					//78 A1
		if (len > format1[20])
		{
			MonthOut = SelfFunc.charAt(s,format1[20]);
		}
		else
		{
			return;
		}
		//int YearOut;					//79-80 I2
		if (len > format1[21])
		{
			temp = SelfFunc.substring(s, format1[21], format1[21] + SelfFunc.min(format2[21], len - format1[21]));
			YearOut = SelfFunc.strToI(temp);
		}
		else
		{
			return;
		}

	}
	public static boolean isLcard(String s)
	{
		if(s.length()<3)
		{
			return false;
		}
		if (s.charAt(0) == 'L')
		{
			if (s.charAt(1) == ' ' )
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
	}
	
}