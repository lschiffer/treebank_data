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

import opennlp.tools.tokenize.*;
import opennlp.tools.util.MarkableFileInputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;

public class Main {

	public static void main(String[] args) throws IOException {
		
		File trainingData = new File("data/test.train");
		File modelFile = new File("data/model.dat");
				
		trainModel(trainingData, modelFile);
		                
		// test created model
		
		InputStream modelIn = new FileInputStream(modelFile);

		try {
			TokenizerModel model = new TokenizerModel(modelIn);

			TokenizerME tokenizer = new TokenizerME(model);

			String tokens[] = tokenizer.tokenize(
					"Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium,"
					+ " totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae"
					+ " vitae dicta sunt explicabo.");
			
			double tokenProbs[] = tokenizer.getTokenProbabilities();
			
			for (int i = 0 ; i != tokens.length ; i++) {
			    System.out.println(tokens[i]);
			    System.out.println(tokenProbs[i]);
			}

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
		ObjectStream<TokenSample> sampleStream = new TokenSampleStream(lineStream);

		TokenizerModel model;

		try {
		  model = TokenizerME.train(sampleStream, new TokenizerFactory("latin", null, true, null), TrainingParameters.defaultParams()) ;
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
