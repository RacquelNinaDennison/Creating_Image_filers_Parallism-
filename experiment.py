# -*- coding: utf-8 -*-
"""
Created on Fri Mar  4 16:19:32 2022

@author: racqu
"""

import os 
import pathlib
import csv
import os
import io

root = pathlib.Path(__file__).resolve()
root = root.parents[1]

def experiment():
    for ds in ['medianSerial','medianParallel','meanSerial','meanParallel']:
        for i in ['example2','example4']:
            for j in ['9','11','21']:
                os.system(f'make run-{ds} ARGS="{i} {i}{j} {j}"')
              
              
        
                         
if __name__=='__main__':
    experiment()
    
    

