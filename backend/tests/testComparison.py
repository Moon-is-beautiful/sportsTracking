from backend import comparison

xCoordinates = [0, 150, 30, 60, 46]
yCoordinates = [20, 60, 50, 45, 50]

actualxCoordinates = [0, 150, 30]
actualyCoordinates = [20, 60, 40]

accuracy = comparison.compareRoutes(
    xCoordinates, yCoordinates, actualxCoordinates, actualyCoordinates
)
print("There is an accuracy of " + str(accuracy) + "%")
