# Read Me file Description
These set of programs are used to compare the speedup time for filtering through photos 
The two filters are the mean filter and the median filter

## ways to run the program 
the make file is used on the linux based operating system 

to run each : 
-make : this will complie the files
- make run-meanSerial ARGS="smallImage smallImage 9" -s // this will silence printing to the command prompt
        * make run-meanSerial will call the serial program 
        * the args are what are passed into the program
        1. args[0] - name of image file 
        2.args[1] - the image name of the output file
        3. args[2] - the window size 


## experiment 
The pyhton experiment script is used to call multiple window sizes on all the programs 