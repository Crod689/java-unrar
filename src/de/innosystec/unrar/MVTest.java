package de.innosystec.unrar;

import java.io.File;
import java.io.FileOutputStream;

import de.innosystec.unrar.rarfile.FileHeader;

public class MVTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String filename = "c:/testdata/test3.rar";
		File f = new File(filename);
		Archive a = null;
		boolean result = false;

		boolean test = true;// test mode

		if (test) {
			String[] pwds = { "114", "1234", "sdfsdfsdfsdf" };

			for (int i = 0; i < pwds.length; i++) {
				long start = System.currentTimeMillis();
				try {
					a = new Archive(f, pwds[i], true);  //test mode, 测试模式
					if (a != null && a.isPass()) { //头部读入完成。如果文件头加密，能正常读入头部，则说明密码正确，无需再做其他验证
						result = true;
					} else {
						result = false;
					}

					if (!a.getMainHeader().isEncrypted()) { //如果文件头没加密，则需解压其中一个文件以验证密码
						FileHeader fh = a.nextFileHeader();
						try {
							a.extractFile(fh, null);
							fh = a.nextFileHeader();
							result = true;
						} catch (Exception e) {
							result = false;
						}
					}
				} catch (Exception e) {
					result = false;
				}

				System.out.println("PWD[" + i + "]:" + pwds[i] + "=" + result
						+ "/" + (System.currentTimeMillis() - start) + "ms");
			}
		} else {
			try{
				a = new Archive(f, "1234", false);  //extract mode, 解压模式
			}catch(Exception e){
				e.printStackTrace();
			}
			
			if(a!=null){
				a.getMainHeader().print();
				FileHeader fh = a.nextFileHeader();
				while (fh != null) {
					try {
						File out = new File("c:/testdata/"
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
