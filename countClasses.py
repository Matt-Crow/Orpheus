from pathlib import Path
import os

def countFiles(pathStr):
    pathObj = Path(pathStr)
    #print("Count files for " + str(pathObj))

    total = 0
    for fileOrDir in pathObj.iterdir():
        #print(fileOrDir)
        if fileOrDir.is_dir():
            total = total + countFiles(fileOrDir)
        else:
            total = total + 1
    print(str(pathObj) + " contains " + str(total) + " files")
    return total

if __name__ == "__main__":
    root = os.path.join(os.getcwd(), "src", "main", "java")
    print(root)
    print(countFiles(root))
