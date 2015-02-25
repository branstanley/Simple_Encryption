import java.io.*;
import org.jblas.*;

public class encrypt {
    static DoubleMatrix code = new DoubleMatrix(new double[][]{{9, 16, 42}, {18, 2, 77}, {19, 32, 65}});
    
    static private DoubleMatrix encryption(String in){
    	if(in == null){
    		System.out.println("Error, input is null");
    		System.exit(-1);
    	}
    	
        int size = (int)Math.ceil((double)in.length()/3.0);
        int i = 0, j = 0;
        double [][] output = new double[3][size];
        DoubleMatrix string_to_encrypt;
        
        for(char t: in.toCharArray()){
            output[j][i] = (double)t;
            if(++i == size){
                ++j;
                i = 0;
            }
        }
        
        string_to_encrypt = new DoubleMatrix(output);
        string_to_encrypt = code.mmul(string_to_encrypt);
        
        return string_to_encrypt;
    }
    static private String decryption(DoubleMatrix in){
        String x = "";
        
        DoubleMatrix decode = Solve.pinv(code);
        DoubleMatrix string_to_decrypt = decode.mmul(in);
        
        for(double t[]: string_to_decrypt.toArray2()){
            for(double r: t){
                x += (char)Math.round(r);
            }
        }
        return x;
    }

    private FileReader is;
    private FileWriter os;
    private String plain_name = "(None)", encrypt_name = "(None)";
    
    private void load_plain_text_file(){
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    	System.out.println("What is the path of the plain text file you're going to read/write with?");
    	try {
			plain_name = br.readLine();
		} catch (IOException e) {
			System.out.println("Error: " + e.getCause());
			plain_name = "(None)";
		}
    }
    private void load_encrypted_file(){
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    	System.out.println("What is the path of the ecrypted file you're going to read/write with?");
    	try{
    		encrypt_name = br.readLine();
    	}catch(IOException e){
			System.out.println("Error: " + e.getCause());
			encrypt_name = "(None)";
    	}
    
    }
    
    private void encrypt_file(){
    	BufferedReader br;
    	BufferedWriter bw;
    	
    	if(plain_name.isEmpty() || encrypt_name.isEmpty()){
    		System.out.println("Please specify a plain text file name and encrypted text file name");
    		return;
    	}
    	try {
            if(is != null)
                    is.close();
            if(os != null)
                    os.close();

            is = new FileReader(plain_name);
            os = new FileWriter(encrypt_name);
            br = new BufferedReader(is);
            bw = new BufferedWriter(os);

            String a;
            double [][] process;

            while((a = br.readLine()) != null){
                process = encryption(a).toArray2();
                a = "";
                for(double x[]: process){
                    for(double y: x){
                        a += y + " ";
                    }
                }
                System.out.println("Attempting to write: " + a);
                bw.write(a + "\n");
                bw.flush();
            }
        } catch (IOException e) {
                System.out.println("Error: " + e.toString());
        } finally{
            try {
                if(is != null){
                    is.close();
                }
                if(os != null){
                    os.close();
                }
            } catch (IOException e) {
                System.out.println();
            }
        }
    }
    
    private void decrypt_file(){
    	BufferedReader br;
		BufferedWriter bw;
		
		if(plain_name.isEmpty() || encrypt_name.isEmpty()){
			System.out.println("Please specify a plain text file name and encrypted text file name");
			return;
		}
		try {
	    	if(is != null)
	    		is.close();
	    	if(os != null)
	    		os.close();
	    	
			is = new FileReader(encrypt_name);
			os = new FileWriter(plain_name);
			
			br = new BufferedReader(is);
			bw = new BufferedWriter(os);
			
			String a;
			double [][] process;
			
			while((a = br.readLine()) != null){
				String [] temp = a.split(" ");
				double [][] numbers = new double[3][(int)Math.ceil((double)temp.length/3.0)];
				
				for(int i = 0; i < 3; ++i){
					for(int j = 0; j < (int)Math.ceil((double)temp.length/3.0); ++j){
						numbers[i][j] = Double.parseDouble(temp[i*(int)Math.ceil((double)temp.length/3.0)+j]);
					}
				}
				String output = decryption(new DoubleMatrix(numbers));
				
				System.out.println("Attempting to write: " + output);
				bw.write(output + "\n");
				bw.flush();
			}
		} catch (IOException e) {
			System.out.println("Error: " + e.getCause());
		} finally{
			try {
                            if(is != null){
				is.close();
                            }
                            if(os != null){
				os.close();
                            }
			} catch (IOException e) {
				System.out.println();
			}
		}
    	
    }
    
    public static void main(String [] in){
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    	int select = 0;																													
    	encrypt e = new encrypt();
    	while(1 == 1){
	    	System.out.println(
	    			"1: Enter plain text filename\n" +
	    			"2: Enter encrypted text filename\n" + 
	    			"3: Encrypt file "+ e.plain_name + "\n" + 
	    			"4: Decrypt file "+ e.encrypt_name +"\n" + 
	    			"0: Exit"
	    	);
	    	
	    	try {
				select = Integer.parseInt(br.readLine());
				
				switch(select){
					case 0:
						System.exit(1);
						break;
					case 1:
						e.load_plain_text_file();
						break;
					case 2:
						e.load_encrypted_file();
						break;
					case 3:
						e.encrypt_file();
						break;
					case 4:
						e.decrypt_file();
						break;
				}
			} catch (IOException ex) {
				
			}
    	}
    }
}
