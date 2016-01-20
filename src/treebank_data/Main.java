package treebank_data;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.Arrays;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerFactory;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.postag.WordTagSampleStream;
import opennlp.tools.sentdetect.SentenceDetectorFactory;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.sentdetect.SentenceSample;
import opennlp.tools.sentdetect.SentenceSampleStream;
import opennlp.tools.tokenize.*;
import opennlp.tools.util.MarkableFileInputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;

public class Main {

	public static void main(String[] args) throws IOException {
		String[] tokens = testTokenization();
		
		testPosTagger(tokens);
	}
	
	
	public static String[] testTokenization() throws IOException {
		
		String tokens[] = null;
		
		File trainingData = new File("output.txt");
		File modelFile = new File("data/model.dat");
				
		trainModel(trainingData, modelFile);
		                		
		InputStream modelIn = new FileInputStream(modelFile);

		try {
			TokenizerModel model = new TokenizerModel(modelIn);

			TokenizerME tokenizer = new TokenizerME(model);			
			
			String lorem = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt"
					+ " ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores"
					+ " et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet? "
					+ "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore"
					+ " et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum."
					+ " Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.";
			
			tokens = tokenizer.tokenize(lorem);
			
			//double tokenProbs[] = tokenizer.getTokenProbabilities();
			
			System.out.println(Arrays.toString(tokens));
						
			try {
				PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("resultTOKENIZER.txt")));
				for (String elem : tokens) {
					out.print(elem);
					out.print(" ");
				}
				out.close();
			}
			catch(IOException e){
				e.printStackTrace();
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
		
		return tokens;

	}
	
	private static void testSentenceDetection() throws IOException {

		File trainingData = new File("outputSENTENCE.txt");
		File modelFile = new File("data/modelSENTENCE.dat");

		trainSentenceDetector(trainingData, modelFile);

		InputStream modelIn = new FileInputStream(modelFile);

		try {
			SentenceModel model = new SentenceModel(modelIn);

			SentenceDetectorME sentenceDetector = new SentenceDetectorME(model);

			String lorem = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt"
					+ " ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores"
					+ " et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet? "
					+ "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore"
					+ " et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum."
					+ " Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.";

			String sentences[] = sentenceDetector.sentDetect(lorem);

			// double tokenProbs[] = tokenizer.getTokenProbabilities();

			System.out.println(Arrays.toString(sentences));

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
	
	
	private static void testPosTagger(String[] tokens) throws IOException {
		
		File trainingData = new File("outputPOS.txt");
		File modelFile = new File("data/modelPOS.dat");
				
		trainPOSTagger(trainingData, modelFile);
		                		
		InputStream modelIn = null;

		try {
			modelIn = new FileInputStream(modelFile);
			POSModel model = new POSModel(modelIn);

			POSTaggerME tagger = new POSTaggerME(model);
	  
			String tags[] = tagger.tag(tokens);			
			//double tokenProbs[] = tokenizer.getTokenProbabilities();
			
			System.out.println(Arrays.toString(tags));
			
			try {
				PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("resultTAGGER.txt")));
				for (int i = 0; i < tokens.length; i++) {
					out.print(tokens[i]);
					out.print(" ");
					out.print(tags[i]);
					out.println();
				}
				out.close();
			}
			catch(IOException e){
				e.printStackTrace();
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
	
	private static void trainSentenceDetector(File trainingFile, File modelFile) throws IOException, FileNotFoundException {
		
		Charset charset = Charset.forName("UTF-8");
		
		ObjectStream<String> lineStream = new PlainTextByLineStream(new MarkableFileInputStreamFactory(trainingFile),
			    charset);
		ObjectStream<SentenceSample> sampleStream = new SentenceSampleStream(lineStream);

		SentenceModel model;

		try {
			
			SentenceDetectorFactory factory = new SentenceDetectorFactory("latin", false, null, null);
			model = SentenceDetectorME.train("latin", sampleStream, factory, TrainingParameters.defaultParams());		  

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
	
	private static void trainPOSTagger(File trainingFile, File modelFile) throws IOException {	
		
		POSModel model = null;

		InputStream dataIn = null;
		try {
		Charset charset = Charset.forName("UTF-8");

			dataIn = new FileInputStream(trainingFile);
			ObjectStream<String> lineStream = new PlainTextByLineStream(new MarkableFileInputStreamFactory(trainingFile),
				    charset);
			ObjectStream<POSSample> sampleStream = new WordTagSampleStream(lineStream);
			
			POSTaggerFactory factory = new POSTaggerFactory(null, null);
			model = POSTaggerME.train("latin", sampleStream, TrainingParameters.defaultParams(), factory);
			
		}
		catch (IOException e) {
		  // Failed to read or parse training data, training failed
		  e.printStackTrace();
		}
		/*finally {
		  if (dataIn != null) {
			  
		    try {
		      dataIn.close();
		    }
		    catch (IOException e) {
		      // Not an issue, training already finished.
		      // The exception should be logged and investigated
		      // if part of a production system.
		      e.printStackTrace();
		    }
		  }
		}*/

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
