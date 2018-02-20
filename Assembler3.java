import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
public class Assembler3{
    public static void main(String[] args) throws IOException{
	    FileReader input = new FileReader("c:\\Program1\\Finalproject2.txt");
		BufferedReader br = new BufferedReader(input);
		br.mark(1);//�b���氵�O��
		FileWriter fw = new FileWriter("c:\\Program1\\Mid.txt");
		FileWriter fw1 = new FileWriter("c:\\Program1\\ObjectCode.txt");
		FileReader input1 = new FileReader("c:\\Program1\\Mid.txt");
		BufferedReader br1 = new BufferedReader(input1);
		String s;
	    String t;
		String z;   //�ন4�줸��
		String z1;  //strAddr��
		String z2;  //total length��
		String zb;  //�g�J������
		String line;//�������
		int i = 0;   //symtb�}�C��
		int x=0; //�p����
		int errorNum=0; //�p����~��
		String[] symtb = new String[99999];
		int[] lc = new int[99999];                //location counter
		lc[1] = 0;
		symtb[0] = "0";
		Hash h = new Hash();
		Hash h1 = new Hash();
		h.init();
		h1.init();
		while((s=br.readLine())!=null) {  
			x++;
			line = String.format("%03d",x);
			//System.out.println(line);
			if(s.indexOf('.')>-1){
			    s = s.substring(0,s.indexOf('.')).trim();
			}
			if(s.getBytes().length !=  s.length()){
					System.out.println("Line"+x+" Syntax error : No Chinese!");
					errorNum++;
			}else{
				String[] arrayA = s.trim().split("\\s+",3);     //�U���}�l�P�_
				if(s.equals("") || arrayA[0].equals(".") || arrayA[0].equals("..") ||arrayA[0].equals(".comment")||(arrayA[0].equals("")&&arrayA[1].equals("."))){
					continue;                                //���Ocomment
				}else{
					i++;
					int flag=0;
					if(h1.get(arrayA[0])==null){
						if(h.get(arrayA[0])!=null){
							flag=1;
						}
						
						if(flag==1){      //�p�G�����ƪ�label�F
							System.out.print("Line"+x+" Label Redefined");
							errorNum++;
							i--;
							continue;
						}
					}
					//i++;
					if(arrayA[0].equals("END")){             //END�ɵ����j��
						if(arrayA.length==2){
							if(h.get(arrayA[1])!=null){
								//lc[i+1] = lc[i] + 3;
								z = String.format("%04x", lc[i]);
								zb = String.format("%06x", lc[i]);
								//System.out.print("END ");
								//System.out.println(z);
								h.put("END",z);
								fw.write(zb+line+" "+s+"\r\n");
								break;
							}else{                           //arrayA[1]���Olabel
								System.out.println("Line"+x+" incorrect start");
								errorNum++;
								//break;
								//System.exit(0);
							}
						}else{
						    System.out.println("Line"+x+" Syntax error : END cannot be the Label");
						}   
					}else{
						if(h.get(arrayA[0])==null){                          //arrayA[0]���Oopcode
							if(arrayA.length==1){
								System.out.println("Line"+x+" Syntax error : Label Only");
								continue;
							}
							if(h.get(arrayA[1])!=null){                      //arrayA[1]�Oopcode
								if(arrayA[1].equals("RSUB")){
									if(arrayA.length==2){
										lc[i+1] = lc[i] + 3;
										z = String.format("%04x", lc[i]);
										zb = String.format("%06x", lc[i]);
										//System.out.print(arrayA[0]+" ");
										//System.out.println(z);
										h.put(arrayA[0],z);
										fw.write(zb+line+" "+s+"\r\n");
								    }else{
									    System.out.println("Line"+x+" No operand allowed after RSUB");
										errorNum++;
									}
								}else if((arrayA.length)==3 && arrayA[2].contains(",X")){  //index
									if(arrayA.length==3){
										if(arrayA[2].contains(",")){
											String[] arrayC = arrayA[2].split(",",2);
											if(arrayC[1].equals("X")){
												lc[i+1] = lc[i] + 3;
												z = String.format("%04x", lc[i]);
												//System.out.print(arrayA[0]+" ");
												//System.out.println(z);
												h.put(arrayA[0],z);
												zb = String.format("%06x", lc[i]);
												fw.write(zb+line+" "+s+"\r\n");
											}else{
												System.out.println("Line"+x+" Syntax error : xxxxx,X");
												errorNum++;
											}
										}else{
											System.out.println("Line"+x+" Syntax error : xxxxx,X");
											errorNum++;
										}
									}else if(arrayA.length==2){
									    System.out.println("Line"+x+" Syntax error : No Operand");
										errorNum++;
									}else{
									    System.out.println("Line"+x+" Syntax error : xxxxx,X");
										errorNum++;
									}
								}else{
									if(arrayA.length==3){
										if(h1.get(arrayA[2])==null){
											lc[i+1] = lc[i] + 3;
											z = String.format("%04x", lc[i]);
											zb = String.format("%06x", lc[i]);
											//System.out.print(arrayA[0]+" ");
											//System.out.println(z);
											h.put(arrayA[0],z);
											fw.write(zb+line+" "+s+"\r\n");
										}else{
										    System.out.println("Line"+x+" Operand can't be OPcode");
											errorNum++;
										}
									}else if(arrayA.length==2){
									    System.out.println("Line"+x+" No Operand");
										errorNum++;										
									}else{
									    System.out.println("Line"+x+" Incorrect number of Operand");
										errorNum++;
									}
								}
							}else{	                                          //arrayA[1]���Oopcode
								if(arrayA[1].equals("START")){
									h.put("lc[1]",arrayA[2]);
									lc[1] = Integer.parseInt(h.get("lc[1]"),16);
									lc[i+1] = lc[i];
									z = String.format("%04x", lc[i]);
									zb = String.format("%06x", lc[i]);
									int StrAddr = Integer.parseInt(arrayA[2]);
									z1 = String.format("%06d", StrAddr);
									h.put("Name",arrayA[0]);
									h.put("StrAddr",z1);
									fw.write(zb+line+" "+s+"\r\n");
								}else if(arrayA[1].equals("END")){
									if(h.get(arrayA[2])!=null){	
										lc[i+1] = lc[i] + 3;
										z = String.format("%04x", lc[i]);
										zb = String.format("%06x", lc[i]);
										//System.out.print("END ");
										//System.out.println(z);
										h.put("END",z);
										h.put(arrayA[0],z);
										fw.write(zb+line+" "+s+"\r\n");
										break;
									}else{
									    System.out.println("Line"+x+" Syntax error : Label doesn't exist");
										errorNum++;
									}
								}else{
									if(arrayA[1].equals("WORD")){
										lc[i+1] = lc[i] + 3;
										z = String.format("%04x", lc[i]);
										zb = String.format("%06x", lc[i]);
										//System.out.print(arrayA[0]+" ");
										//System.out.println(z);
										h.put(arrayA[0],z);
										fw.write(zb+line+" "+s+"\r\n");
									}else if(arrayA[1].equals("BYTE")){
										if(arrayA[2].contains("C'")){
											String ASCIII = arrayA[2].replace("C'","").replace("'","");
											lc[i+1] = lc[i] + ASCIII.length();
										}else if(arrayA[2].contains("X'")){
											String ASCIV = arrayA[2].replace("X'","").replace("'","");
											if(ASCIV.length()%2==1){
												System.out.println();
												System.out.print("Line"+x+" X'' must be even");
												errorNum++;
												continue;
											}
											lc[i+1] = lc[i] + ASCIV.length()/2;
										}else{
										    System.out.println("Line"+x+" Syntax error : must be C'' or X''");
										}
										z = String.format("%04x", lc[i]);
										zb = String.format("%06x", lc[i]);
										//System.out.print(arrayA[0]+" ");
										//System.out.println(z);
										h.put(arrayA[0],z);
										fw.write(zb+line+" "+s+"\r\n");
									}else if(arrayA[1].equals("RESW")){
										int p;
										p = Integer.parseInt(arrayA[2]);
										lc[i+1] = lc[i] + 3*p;
										z = String.format("%04x", lc[i]);
										zb = String.format("%06x", lc[i]);
										//System.out.print(arrayA[0]+" ");
										//System.out.println(z);
										h.put(arrayA[0],z);
										fw.write(zb+line+" "+s+"\r\n");
									}else if(arrayA[1].equals("RESB")){
										int q;
										q = Integer.parseInt(arrayA[2]);
										lc[i+1] = lc[i] + 1*q;
										z = String.format("%04x", lc[i]);
										zb = String.format("%06x", lc[i]);
										//System.out.print(arrayA[0]+" ");
										//System.out.println(z);
										h.put(arrayA[0],z);
										fw.write(zb+line+" "+s+"\r\n");                   
									}else{                                          //arrayA[1]�O��Lopcode
										System.out.println("Line"+x+" OPcode not Found");
										errorNum++;
										continue;
									}
								}
							}
						}else{                                //arrayA[0]�Oopcode
							if(arrayA[0].equals("RSUB")){
							    if(arrayA.length==1){
									lc[i+1] = lc[i] + 3;
									z = String.format("%04x", lc[i]);
									zb = String.format("%06x", lc[i]);
									fw.write(zb+line+" "+s+"\r\n");
								}else{
									System.out.println("Line"+x+" No operand allowed after RSUB");
									errorNum++;
								}
							}else if(arrayA.length==2 && arrayA[1].contains(",X")){
							    if(arrayA.length==2){
									if(arrayA[1].contains(",")){
										String[] arrayC = arrayA[1].split(",",2);
										if(arrayC[1].equals("X")){
											lc[i+1] = lc[i] + 3;
											z = String.format("%04x", lc[i]);
											zb = String.format("%06x", lc[i]);
											fw.write(zb+line+" "+s+"\r\n");
										}else{
									    System.out.println("Line"+x+" Syntax error : xxxxx,X");
										errorNum++;
										}
									}else{
										System.out.println("Line"+x+" Syntax error : xxxxx,X");
										errorNum++;
									}
								}else if(arrayA.length==1){
								    System.out.println("Line"+x+" Syntax error : No Operand");
									errorNum++;
								}else{
								    System.out.println("Line"+x+" Syntax error : xxxxx,X");
									errorNum++;
								}
							}else{                                     //arrayA[0]�O��Lopcode
								if(arrayA.length==2){
									if(h1.get(arrayA[1])==null){
										lc[i+1] = lc[i] + 3;
										z = String.format("%04x", lc[i]);
										zb = String.format("%06x", lc[i]);
										fw.write(zb+line+" "+s+"\r\n");
										continue;
									}else{
									    System.out.println("Line"+x+" Operand can't be OPcode");
										errorNum++;
									}
                                }else if(arrayA.length==1){
                                    System.out.println("Line"+x+" No Operand");
									errorNum++;
								}else{
                                    System.out.println("Line"+x+" Incorrect number of Operand");
									errorNum++;
								}								
						    }
						}
					}	
				}
			}
        }			
		System.out.println();
	    fw.flush();
		//////////////////////////////////////////////////////////////////////////////////////////pass 2
		if(errorNum==0){
			int length = Integer.parseInt(h.get("END"),16)-Integer.parseInt(h.get("StrAddr"),16);
			int MidCount = 0;
			z2 = String.format("%06x", length);       //�w�g�O16�i��A�ҥHd�N�n(���ΦA�ഫ)
			if(errorNum==0){
				System.out.println("H"+ h.get("Name")+"  "+ h.get("StrAddr")+z2);
				fw1.write("H"+ h.get("Name")+"  "+ h.get("StrAddr")+z2+"\r\n");
			}
			//br.reset();
			int countlength=0;
			String total="";   //�u���p�����
			String total1="";  //���ժ��ץ�
			String reg="";		//�W�L60��
			String start=""; 
			int count1 = 0;   //�L�X�Ĥ@��t record�P�_��
		
			while((t=br1.readLine())!=null) {  
				//MidCount++;
				String[] arrayB = t.substring(10).trim().split("\\s+",3);  
				if(t.substring(9).equals("") || arrayB[0].equals(".") || arrayB[0].equals("..") ||arrayB[0].equals(".comment")||(arrayB[0].equals("")&&arrayB[1].equals("."))){
					continue;
				}else{  	
					if(arrayB[0].equals("END")){             //END�ɵ����j��		    
						//break;
					}else {
						if(h1.get(arrayB[0])!=null){                //arrayB[0]�Oopcode
							if(arrayB[0].contains("RSUB")){                 //arrayB[0]�ORSUB
								total1+="4C0000";
								if(total=="" && count1!=0){
									System.out.print(t.substring(0,6));
									fw1.write(t.substring(0,6));
									total+="4C0000";
								}else{
									if(total1.length()<=60){
										total+="4C0000";
									}else{
										reg = "4C0000";
										start = t.substring(0,6);    
									}						
								}
							}else if(arrayB.length==2 && arrayB[1].contains(",X")){  //arrayB[0]�O index addressing
									if(arrayB[1].contains(",")){
										String[] arrayC = arrayB[1].split(",",2);
										if(arrayC[1].equals("X")){
											//String str1 = arrayB[1];
											int indexAddr =  Integer.parseInt(h.get(arrayC[0]),16)+32768;  //+8000(16)
											String z3 = String.format("%04x", indexAddr);
											total1+=h.get(arrayB[0])+z3;
											if(total=="" && count1!=0){
												System.out.print(t.substring(0,6));
												fw1.write(t.substring(0,6));
												total+=h.get(arrayB[0])+z3;
											}else{
												if(total1.length()<=60){
													total+=h.get(arrayB[0])+z3;
												}else{
													reg = h.get(arrayB[0])+z3;
													start = t.substring(0,6);    
												}						
											}
										}else{
											System.out.println("MLine"+t.substring(6,9)+" Syntax error : xxxxx,X");
											errorNum++;
										}
									}else{
										System.out.println("MLine"+t.substring(6,9)+" Syntax error : xxxxx,X");
										errorNum++;
									}
							}else{                                  //arrayB[0]�O��Lopcode
								if(h.get(arrayB[1])!=null){        //arrayB[1]�OLabel
									total1+=h.get(arrayB[0])+h.get(arrayB[1]);
									if(total=="" && count1!=0){
										System.out.print(t.substring(0,6));
										fw1.write(t.substring(0,6));
										total+=h.get(arrayB[0])+h.get(arrayB[1]);
									}else{
										if(total1.length()<=60){
											//System.out.print(h.get(arrayB[0])+h.get(arrayB[1]));
											total+=h.get(arrayB[0])+h.get(arrayB[1]);
										}else{
											reg = h.get(arrayB[0])+h.get(arrayB[1]);
											start = t.substring(0,6);    
										}						
									}	
								}else{
									System.out.println();
									System.out.println("MLine"+t.substring(6,9)+": Label Undefined");
									errorNum++;
								}
							}
						}else{                                      //arrayB[0]���Oopcode
							if(h1.get(arrayB[1])!=null){            //arrayB[1]�Oopcode
								if(arrayB[1].equals("RSUB")){       //arrayB[1]�ORSUB
									total1+="4C0000";
									if(total=="" && count1!=0){
										System.out.print(t.substring(0,6));
										fw1.write(t.substring(0,6));
										total+="4C0000";
									}else{
										if(total1.length()<=60){
											total+="4C0000";
										}else{
											reg = "4C0000";
											start = t.substring(0,6);    
										}						
									}
								}else if(arrayB.length==3 && arrayB[2].contains(",X")){  //arrayB[1]�O index addressing
									if(arrayB[2].contains(",")){
										String[] arrayC = arrayB[2].split(",",2);
										if(arrayC[1].equals("X")){
											//String str1 = arrayB[1];
											int indexAddr =  Integer.parseInt(h.get(arrayC[0]),16)+32768;  //+8000(16)
											String z3 = String.format("%04x", indexAddr);
											total1+=h.get(arrayB[1])+z3;
											if(total=="" && count1!=0){
												System.out.print(t.substring(0,6));
												fw1.write(t.substring(0,6));
												total+=h.get(arrayB[1])+z3;
											}else{
												if(total1.length()<=60){
													total+=h.get(arrayB[1])+z3;
												}else{
													reg = h.get(arrayB[1])+z3;
													start = t.substring(0,6);    
												}						
											}
										}else{
											System.out.println("MLine"+t.substring(6,9)+" Syntax error : xxxxx,X");
											errorNum++;
										}
									}else{
										System.out.println("MLine"+t.substring(6,9)+" Syntax error : xxxxx,X");
										errorNum++;
									}
								}else {                             //arrayB[1]�O��Lopcode
									if(h.get(arrayB[2])!=null){         //arrayB[2]�O�X�k��data
										total1+=h.get(arrayB[1])+h.get(arrayB[2]);
										if(total=="" && count1!=0){
											System.out.print(t.substring(0,6));
											fw1.write(t.substring(0,6));
											total+=h.get(arrayB[1])+h.get(arrayB[2]);
										}else{
											if(total1.length()<=60){
												total+=h.get(arrayB[1])+h.get(arrayB[2]);
											}else{
												reg = h.get(arrayB[1])+h.get(arrayB[2]);
												start = t.substring(0,6);    
											}						
										}
									}else{
										System.out.println(arrayB[2]+" Undefined");
										errorNum++;
									}
								}
							}else{                                  //arrayB[1]���Oopcode
								if(t.contains("START")){             
									continue;
								}else{
									if(arrayB[1].equals("WORD")){
										int wd = Integer.parseInt(arrayB[2]);
										String z10 = String.format("%06x", wd);
										total1+=z10;
										if(total=="" && count1!=0){
											System.out.print(t.substring(0,6));
											fw1.write(t.substring(0,6));
											total+=z10;
										}else{
											if(total1.length()<=60){
												total+=z10;
											}else{
												reg = z10;
												start = t.substring(0,6);    
											}						
										}
									}else if(arrayB[1].equals("BYTE")){
										if(arrayB[2].contains("C'")){
											String ASC = arrayB[2].replace("C'","").replace("'","");
											String ASCII = "";
											for(int c = 0; c < ASC.length(); c++ ) {               //EOF��454F46
												char d = ASC.charAt( c ); 
												int j = (int) d; 
												String hexstr = Integer.toHexString(j);
												ASCII += hexstr;	
											}
											total1 += ASCII;
											if(total=="" && count1!=0){
												System.out.print(t.substring(0,6));
												fw1.write(t.substring(0,6));
												total+=ASCII;
											}else{
												if(total1.length()<=60){
													total+=ASCII;
												}else{
													reg = ASCII;
													start = t.substring(0,6);    
												}						
											}							
										}else if(arrayB[2].contains("X'")){                                              //���OC''
											total1+= arrayB[2].replace("X'","").replace("'",""); 
											if(total=="" && count1!=0){
												System.out.print(t.substring(0,6));
												fw1.write(t.substring(0,6));
												total+= arrayB[2].replace("X'","").replace("'","");
											}else{
												if(total1.length()<=60){
													total+= arrayB[2].replace("X'","").replace("'","");
												}else{
													reg = arrayB[2].replace("X'","").replace("'","");
													start = t.substring(0,6);    
												}						
											}
										}else{
											System.out.println("MLine"+t.substring(6,9)+" error : C'' or X''");
										}
									}else if(arrayB[1].equals("RESW")){
									}else if(arrayB[1].equals("RESB")){
									}else if(arrayB[1].equals("END")){
									}else{
										System.out.println("MLine"+t.substring(6,9)+" OPcode Not Found");
										errorNum++;
									}
								}
							}			
						}
					}
				}
				countlength = total.length()/2;
				String cntleg = String.format("%02x",countlength);
				if(errorNum==0){	
					if(total1.length()>60 && count1==0){
						System.out.println("T"+h.get("StrAddr")+cntleg+total); //T+�_�l��m+����+Trecord
						fw1.write("T"+h.get("StrAddr")+cntleg+total+"\r\n");
						total1="";
						total="";
						total1+=reg;
						total+=reg;
						count1 = 1;
						System.out.print("T"+start);
						fw1.write("T"+start);
					}else if(total1.length()>60 && count1!=0){
						System.out.println(cntleg+total); //T+�_�l��m+����+Trecord
						fw1.write(cntleg+total+"\r\n");
						total1="";
						total="";
						total1+=reg;
						total+=reg;
						System.out.print("T"+start);
						fw1.write("T"+start);
					}else if(total1.length()<=60 && t.contains("RESW") && !total.equals("")){
						if(count1==0){
							System.out.println("T"+h.get("StrAddr")+cntleg+total); //T+�_�l��m+����+Trecord
							fw1.write("T"+h.get("StrAddr")+cntleg+total+"\r\n");
							total1="";
							total="";
							count1 = 1;
							System.out.print("T");
							fw1.write("T");
						}else{
							System.out.println(cntleg+total); //T+�_�l��m+����+Trecord
							fw1.write(cntleg+total+"\r\n");
							total1="";
							total="";
							System.out.print("T");
							fw1.write("T");
						}
					}else if(total1.length()<=60 && t.contains("RESB") && !total.equals("")){
						if(count1==0){
							System.out.println("T"+h.get("StrAddr")+cntleg+total); //T+�_�l��m+����+Trecord
							fw1.write("T"+h.get("StrAddr")+cntleg+total+"\r\n");
							total1="";
							total="";
							count1 = 1;
							System.out.print("T");
							fw1.write("T");
						}else{
							System.out.println(cntleg+total); //T+�_�l��m+����+Trecord
							fw1.write(cntleg+total+"\r\n");
							total1="";
							total="";
							System.out.print("T");
							fw1.write("T");
						}
					}else if(arrayB[0].equals("END")){
						System.out.println(cntleg+total); //T+�_�l��m+����+Trecord
						fw1.write(cntleg+total+"\r\n");
						int enter = Integer.parseInt(h.get(arrayB[1]),16);
						String ProEnter = String.format("%06x",enter);
						System.out.print("E" + ProEnter);
						fw1.write("E" + ProEnter);
						fw1.flush();
						break;
					}else if(arrayB[0]!=null && arrayB.length==3){
					    if(arrayB[1].equals("END")){
						    System.out.println(cntleg+total); //T+�_�l��m+����+Trecord
							fw1.write(cntleg+total+"\r\n");
							int enter = Integer.parseInt(h.get(arrayB[2]),16);
							String ProEnter = String.format("%06x",enter);
							System.out.print("E" + ProEnter);
							fw1.write("E" + ProEnter);
							fw1.flush();
							break;
						}
					}else{
					}
				}				
			}
		}
	}
}