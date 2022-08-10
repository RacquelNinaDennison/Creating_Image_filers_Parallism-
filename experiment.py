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
    for ds in ['meanSerial', 'meanParallel','medianSerial','medianParallel']:
        for i in ['1','2','3','4']:
          for j in ['9','27','31','45']:
              os.system(f'make run-{ds} ARGS="example{i} example{i} {j}"'
              
              
            ) 
        
        
                         
if __name__=='__main__':
    experiment()
    
    

