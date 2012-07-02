package de.innosystec.unrar;

import java.io.File;
import java.io.FileOutputStream;

import de.innosystec.unrar.rarfile.FileHeader;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String file = null;
		String password = null;
		boolean test = false;
		if(args.length==4||args.length==5){
			for(int i=0;i<args.length;i++){
				if("-f".equals(args[i].toLowerCase())){
					file = args[i+1];
					continue;
				}
				
				if("-p".equals(args[i].toLowerCase())){
					password = args[i+1];
					continue;
				}
				
				if("-t".equals(args[i].toLowerCase())){
					test = true;
					continue;
				}
			}
		}
				
		if(file==null||password==null){
			System.out.println("Usage:");
			System.out.println("-f	<filename>");
			System.out.println("-p	<password>");
			System.out.println("-t	Option, run in test mode.");
			System.exit(0);
		}
		
		File f = new File(file);
		if(!f.exists()){
			System.out.println("File " + file +" does not exiest.");
			System.exit(-1);
		}
		Archive a = null;
		boolean result = false;

		if (test) {
				long start = System.currentTimeMillis();
				try {
					a = new Archive(f, password, true);  //test mode
					result = a.test();
				} catch (Exception e) {
					result = false;
				}

				System.out.println("PWD:" + password + "=" + result
						+ "/" + (System.currentTimeMillis() - start) + "ms");
		} else {
			try{
				a = new Archive(f, password, false);  //extract mode
			}catch(Exception e){
				e.printStackTrace();
			}
			
			if(a!=null){
				a.getMainHeader().print();
				FileHeader fh = a.nextFileHeader();
				while (fh != null) {
					try {
						File out = new File(f.getParent()+"/"
								+ fh.getFileNameString().trim());
						System.out.println(out.getAbsolutePath());
						FileOutputStream os = new FileOutputStream(out);
						a.extractFile(fh, os);
						os.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
					fh = a.nextFileHeader();
				}
			}
		}
	}

}
