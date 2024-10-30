from dtaidistance import dtw_ndim
import numpy as np
import math

def compareRoutes(userX:list, userY:list, expectedX:list, expectedY:list): #User X and Y are the user's coordinates while expected X is the actual route.
    userCoordinates = [] #stores the coordinates as an x,y array
    for i in range(len(userX)):
        tempX = userX[i]
        tempY = userY[i]
        userCoordinates.append([tempX, tempY])
    
    expectedCoordinates = []
    for i in range(len(expectedX)): 
        tempX = expectedX[i]
        tempY = expectedY[i]
        expectedCoordinates.append([tempX, tempY])
        
    series1 = np.array(userCoordinates,dtype=np.double)
    series2 = np.array(expectedCoordinates, dtype=np.double)
    
    dist = dtw_ndim.distance(series1, series2) #distance between the two series
    expectedLength = 0 #length of the actual path
    for i in range(1,len(expectedCoordinates)):
        expectedLength += math.sqrt((expectedX[i] - expectedX[i-1]) ** 2 + (expectedY[i] - expectedY[i-1]) ** 2)
    
    accuracy = 100 - (dist/expectedLength) * 100
    if(accuracy < 0):
        accuracy = 0
    return accuracy #returns accuracy as a percent


xCoordinates = [0, 150, 30, 60, 46]
yCoordinates = [20, 60, 50, 45, 50]

actualxCoordinates = [0, 150, 30]
actualyCoordinates = [20, 60, 40]

accuracy = compareRoutes(xCoordinates, yCoordinates, actualxCoordinates, actualyCoordinates)
print ("There is an accuracy of " + str(accuracy) + "%")