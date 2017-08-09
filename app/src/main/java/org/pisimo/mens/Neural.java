 //Neural Classes:
    class Layer
    {
        //Activation functions
        /** None. Just outputs the sum of all inputs */
        public static final int ACTIVATION_NONE = 0;
        /** Hyperbolic tangent. Output between -1 and 1 */
        public static final int ACTIVATION_TANH = 1;
        /** Sigmoid / Logistic function. Output between 0 and 1 */
        public static final int ACTIVATION_SIGMOID = 2;
        /** Step function. Everything smaller than 0 becomes -1, everything else 1 */
        public static final int ACTIVATION_HEAVISIDE_STEP_FUNCTION_N1_1 = 3;
        /** Step function. Everything smaller than 0 becomes 0, everything else 1 */
        public static final int ACTIVATION_HEAVISIDE_STEP_FUNCTION_0_1 = 4;
        /** Rectified Linear Unit (ReLU). Everything below 0 becomes 0 */
        public static final int ACTIVATION_RECTIFIED_LINEAR_UNIT = 5;


        //Some numbers to define the size
        private final int numberOfInputs;
        private final int numberOfOutputs;
        // Weights. Every input gets an array of weights. One for every output
        private double[][] weights;
        // Activation function. Applied in every neuron on the sum of the weighted inputs
        private final int activationFunction;

        public Layer(int numberOfInputs, int numberOfOutputs,
                     int activationFunction) throws Exception
        {
            if(numberOfInputs <= 0)
                throw new Exception("Number of inputs must be 1 or more!");
            if(numberOfOutputs <= 0)
                throw new Exception("Number of outputs must be 1 or more!");


            this.numberOfInputs = numberOfInputs;
            this.numberOfOutputs = numberOfOutputs;
            this.activationFunction = activationFunction;
            weights = new double[numberOfInputs][numberOfOutputs];
        }
        public double[] calculate(double[] input) throws Exception
        {
            if(input.length != numberOfInputs)
                throw new Exception("Number of inputs doesn't fit!");


            double[] output = new double[numberOfOutputs];

            //Sum all weighted inputs for every output neuron
            //For every output ...
            for(int j=0; j<output.length; j++) {
                //... sum all the weighted inputs
                for(int i=0; i<input.length; i++) {
                    output[j] += weights[i][j] * input[i];
                }
            }

            //Apply the activation function on every output neuron
            for(int i=0; i<output.length; i++) {
                switch(activationFunction) {
                    //None. Just outputs the sum of all inputs
                    case ACTIVATION_NONE:
                        break;

                    //Hyperbolic tangent. Output between -1 and 1
                    case ACTIVATION_TANH:
                        output[i] = Math.tanh(output[i]);
                        break;

                    //Sigmoid / Logistic function. Output between 0 and 1
                    case ACTIVATION_SIGMOID:
                        output[i] = 1 / (1 + Math.exp(-output[i]));
                        break;

                    //Step function. Everything smaller than 0 becomes -1, everything else 1
                    case ACTIVATION_HEAVISIDE_STEP_FUNCTION_N1_1:
                        if(output[i] >= 0) {
                            output[i] = 1;
                        } else {
                            output[i] = 0;
                        }
                        break;

                    //Step function. Everything smaller than 0 becomes 0, everything else 1
                    case ACTIVATION_HEAVISIDE_STEP_FUNCTION_0_1:
                        if(output[i] >= 0) {
                            output[i] = 1;
                        } else {
                            output[i] = 0;
                        }
                        break;

                    //Rectified Linear Unit (ReLU). Everything below 0 becomes 0
                    case ACTIVATION_RECTIFIED_LINEAR_UNIT:
                        if(output[i] < 0) {
                            output[i] = 0;
                        }
                        break;
                                }
            }
            return output;
        }

        public void setWeights(double[][] weights) throws Exception
        {
            //Is there 1 weight array for every input?
            if(weights.length != this.weights.length)
                throw new Exception("Wrong size!");

            //As many weights as outputs, for every input?
            for(int i=0; i<this.weights.length; i++) {
                if(weights[i].length != this.weights[i].length) {
                    throw new Exception("Wrong size!");
                }
            }

            this.weights = weights;
        }
        public double[][] getWeights()
        {
            return weights;
        }

        //Output a more or less readable representation
        @Override
        public String toString()
        {
            //Construct a beautiful string!
            StringBuilder result = new StringBuilder();

            //All the weights
            //Every column shows all the weights for one input
            //Every row show all the weights for one output
            for(int j = 0; j< numberOfOutputs; j++) {
                for(int i = 0; i< numberOfInputs; i++) {
                    result.append(String.format("| %.5f ", weights[i][j]));
                }
                result.append("|\n");
            }

            //Draw an o for every output with an "/ \" above, if there are more outputs
            //than inputs (layer gets bigger),
            //or an "\ /" if it gets smaller or "|" if the size stays the same
            for(int i = 0; i< numberOfOutputs; i++)
            {
                if(numberOfOutputs < numberOfInputs) {
                    result.append("    \\ /   ");
                } else if(numberOfOutputs > numberOfInputs) {
                    result.append("    / \\   ");
                } else {
                    result.append("     |    ");
                }
            }
            result.append("\n");


            //Add an circle to show the neurons
            for(int i = 0; i< numberOfOutputs; i++) {
                result.append("     o    ");
            }
            result.append("\n");

            return result.toString();
        }

    }

    public class Neural {  //extends MultiDexApplication {
        //Layers. All the Layers in the Network
        private final Layer[] layers;

        public Neural(int numberOfInputs, int[] layerSizes,
                      int[] activationFunctions) throws Exception {

            if (layerSizes.length <= 0)
                throw new Exception("Number of hidden layers must be more than 0!");
            if (activationFunctions.length != layerSizes.length)
                throw new Exception
                        ("There must be as many activation functions as hidden layers!");

            layers = new Layer[layerSizes.length];
            layers[0] = new Layer(numberOfInputs, layerSizes[0],
                    activationFunctions[0]);
            //Hidden layers + output layer
            for (int i = 1; i < layerSizes.length; i++) {
                layers[i] = new Layer(layerSizes[i - 1], layerSizes[i],
                        activationFunctions[i]);
            }
        }
        public double[] calculate(double[] input) throws Exception {
            //Take the input ...
            double[] temp = input;
            //... and feed it into a layer and feed  the output into the next layer
            for (Layer layer : layers) {
                temp = layer.calculate(temp);
            }
            return temp;
        }

        public double cost(double[][] input, double[][] output) throws Exception {
            if (input.length != output.length)
                throw new Exception("Number of inputs must equal the number of given outputs!");
            double cost = 0;

            //Sum the costs for every data set
            for (int j = 0; j < input.length; j++) {
                //Calculate the actual result
                double[] result = calculate(input[j]);
                double error = 0;

                //And compare every actual output with the wanted output
                for (int i = 0; i < result.length; i++) {
                    error += (output[j][i] - result[i]) * (output[j][i] - result[i]);
                }
                cost += error;
            }
            cost *= 0.5;
            return cost;
        }

       public void loadFromFile(InputStream is) throws Exception {
            BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
            String line = br.readLine();
            int l = 0;
            int la = 0;
            double[][] W = new double[layers[l].getWeights().length][];
            do {

                if (line.indexOf("#") == -1) {
                    String[] splitted = line.split(" ");
                    double[] ws = new double[layers[l].getWeights()[la].length];
                    for (int i = 0; i != ws.length; i++) {
                        if (splitted[i] == null || splitted[i].isEmpty()) break;
                        ws[i] = Double.valueOf(splitted[i]);
                    }
                    W[la] = ws;
                    la++;


                } else {

                    layers[l].setWeights(W);

                    la = 0;
                    l++;
                    if (l == layers.length) break;
                    System.out.println(layers.length);
                    for (int i = 0; i != layers.length; i++)
                        System.out.println(layers[i].getWeights().length);
                    //  System.out.println(layers[l].getWeights().length);
                    W = new double[layers[l].getWeights().length][];

                }


                line = br.readLine();
            } while (line != null);
            br.close();
        }


    }
