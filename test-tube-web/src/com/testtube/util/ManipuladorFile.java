package com.testtube.util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.primefaces.event.FileUploadEvent;

/**
 * Classe desenvolvida para auxílio na manipulação de arquivos.
 * 
 * @author Rafael Gouveia da Silva
 * @since JDK1.7
 * @version 1.2
 * 
 * */
public class ManipuladorFile {

	public ManipuladorFile() {

	}

	public boolean verificaDiretorio(String caminho) {
		java.io.File dir = new java.io.File(caminho);
		if (dir.exists()) {
			return true;
		}
		return false;
	}

	public boolean criarDiretorio(String caminho) throws Exception {
		java.io.File dir = new java.io.File(caminho);
		return dir.mkdir();
	}

	public boolean renomear(String caminho, String nomeAntigo, String nomeNovo) throws Exception {
		java.io.File dir = new java.io.File(caminho + nomeAntigo);
		java.io.File dir2 = new java.io.File(caminho + nomeNovo);
		return dir.renameTo(dir2);
	}

	public String verificaDiretorios(String path) throws Exception {
		boolean retorno = false;
		retorno = verificaDiretorio(path);
		if (retorno == false) {
			criarDiretorio(path);
		}
		return path;
	}

	@SuppressWarnings("resource")
	public File criaArquivoFisico(String caminhoBase, File file) {
		try {
			FileInputStream fis = new FileInputStream(file.getAbsolutePath());
			File aux = new File(caminhoBase + file.getName());
			BufferedInputStream bis = new BufferedInputStream(fis);
			FileOutputStream fout = new FileOutputStream(aux);
			while (bis.available() != 0) {
				fout.write(bis.read());
			}
			fout.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return file;
	}

	public File criaArquivoFisico(FileUploadEvent event, String caminhoBase) throws Exception {
		File file = null;
		File aux = null;
		String path = verificaDiretorios(caminhoBase);
		try {
			file = new File(event.getFile().getFileName());

			path += "\\" + file.getName();
			aux = new File(path);

			// Preparo as informações para salvar em banco de dados
			BufferedInputStream in = new BufferedInputStream(event.getFile().getInputstream());
			FileOutputStream fout = new FileOutputStream(aux);
			while (in.available() != 0) {
				fout.write(in.read());
			}
			fout.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return file;
	}

	/**
	 * Remove um arquivo que se encontra em determinado caminho.
	 * 
	 * @param caminho
	 *            - Caminho do arquivo
	 * @return <code>boolean<code>
	 */
	public boolean removerArquivo(String caminhoBase, String nomeArquivo, String user) {
		File f = new File(caminhoBase + nomeArquivo);
		return f.delete();
	}

	public static synchronized void downloadFile(String filename, String fileLocation, String mimeType, FacesContext facesContext) {

		ExternalContext context = facesContext.getExternalContext(); // Context
		String path = fileLocation; // Localizacao do arquivo
		File file = new File(path); // Objeto arquivo mesmo

		HttpServletResponse response = (HttpServletResponse) context.getResponse();
		response.setHeader("Content-Disposition", "attachment;filename=\"" + filename + "\"");
		response.setContentLength((int) file.length()); // O tamanho do arquivo
		response.setContentType(mimeType); // e o tipo

		try {
			FileInputStream in = new FileInputStream(file);
			OutputStream out = response.getOutputStream();

			byte[] buf = new byte[(int) file.length()];
			int count;
			while ((count = in.read(buf)) >= 0) {
				out.write(buf, 0, count);
			}
			in.close();
			out.flush();
			out.close();
			facesContext.responseComplete();
		} catch (IOException ex) {
			System.out.println("Erro ao fazer o download: " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	public static void saveImage(String imageUrl, String destinationFile) throws IOException {
		URL url = new URL(imageUrl);
		InputStream is = url.openStream();
		OutputStream os = new FileOutputStream(destinationFile);

		byte[] b = new byte[2048];
		int length;

		while ((length = is.read(b)) != -1) {
			os.write(b, 0, length);
		}

		is.close();
		os.close();
	}

	@SuppressWarnings("unused")
	private void redimensionaImg(File img, String caminho) throws IOException {
		BufferedImage imagem = null;
		try {
			imagem = ImageIO.read(img);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		int new_w = 200, new_h = 200;
		BufferedImage new_img = new BufferedImage(new_w, new_h, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = new_img.createGraphics();

		g.drawImage(imagem, 0, 0, new_w, new_h, null);
		ImageIO.write(new_img, "JPG", new File(caminho + "\\" + img.getName()));
	}

}