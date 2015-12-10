package treebank_data;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Arrays;

import opennlp.tools.tokenize.*;
import opennlp.tools.util.MarkableFileInputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;

public class Main {

	public static void main(String[] args) throws IOException {
		
		File trainingData = new File("output.txt");
		File modelFile = new File("data/model.dat");
				
		trainModel(trainingData, modelFile);
		                		
		InputStream modelIn = new FileInputStream(modelFile);

		try {
			TokenizerModel model = new TokenizerModel(modelIn);

			TokenizerME tokenizer = new TokenizerME(model);

			String tokens[] = tokenizer.tokenize(
					"Sed ut perspiciatis unde omnis iste natus error sit voluptatemque accusantiumque doloremque laudantiumque,"
					+ " totam rem aperiam, eaque ipsa quae ab illo inventore veritatisque et quasi architecto beataeque"
					+ " vitae dicta sunt explicabo. ");
			
			//double tokenProbs[] = tokenizer.getTokenProbabilities();
			
			System.out.println(Arrays.toString(tokens));

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (modelIn != null) {
				try {
					modelIn.close();
				} catch (IOException e) {
				}
			}
		}	

	}

	private static void trainModel(File trainingFile, File modelFile) throws IOException, FileNotFoundException {
		
		Charset charset = Charset.forName("UTF-8");
		ObjectStream<String> lineStream = new PlainTextByLineStream(new MarkableFileInputStreamFactory(trainingFile),
		    charset);
		//ObjectStream<String> lineStream = new PlainTextByLineStream(new FileInputStream(trainingFile),
		//	    charset);
		ObjectStream<TokenSample> sampleStream = new TokenSampleStream(lineStream);

		TokenizerModel model;
		
		try {
			
			TrainingParameters params = TrainingParameters.defaultParams();
			//params.put("CUTOFF_PARAM", "1");
		    model = TokenizerME.train(sampleStream, new TokenizerFactory("latin", null, false, null), params);
			//model = TokenizerME.train("latin", sampleStream, false, params);
			
		}
		finally {
			sampleStream.close();
		}

		OutputStream modelOut = null;
		try {
			modelOut = new BufferedOutputStream(new FileOutputStream(modelFile));
			model.serialize(modelOut);
		} finally {
			if (modelOut != null)
				modelOut.close();
		}
	}

}
