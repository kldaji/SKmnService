import subprocess
import sys
from datetime import datetime
import pandas as pd
import re

apikey = "VeZTw8-B4TfmOdLXH3H1517CkBjP9AG8dHeELrPS_2jV"
url = "https://api.kr-seo.speech-to-text.watson.cloud.ibm.com/instances/28994288-68ad-443d-aa0c-1444836198ba"

filename = datetime.now().strftime("%Y%m%d%H%M%S%f")


def link_to_mp3(address):
    command = (
        f"youtube-dl --extract-audio --audio-format mp3 -o result/{filename}.xfloat-array "
        + address
    )
    subprocess.run(command, shell=True)


def generate_text(filepath):
    out_file = filepath.split(".")[0] + ".txt"
    command = f"""curl -X POST -u "apikey:{apikey}" --header "Content-Type: audio/mp3" --data-binary @{filepath} > {out_file} "{url}/v1/recognize" """
    subprocess.run(command, shell=True)
    return out_file


def txt_preprocessing(filename):
    file = open(filename)
    txt = ""
    for t in file:
        txt += t.strip("\n")
    txt = txt.replace("true", "True")
    dic = eval(txt)
    results = dic["results"]
    n_transcripts = len(results)  # 스크립트 수
    transcripts = list()

    for small_dic in results:
        transcripts.append(small_dic["alternatives"][0]["transcript"])

    p1 = re.compile("%\S*\s")  # %HESITATION
    p2 = re.compile("[*]+")  # ****
    new_transcripts = list()
    for t in transcripts:
        t = re.sub(p1, "", t)
        t = re.sub(p2, "", t)
        new_transcripts.append(t)

    total_scripts = "".join(new_transcripts)
    df = pd.DataFrame({"Text": [total_scripts]})
    df.to_csv("{0}.csv".format(filename.split(".")[0]), index=[0], encoding="cp949")


if __name__ == "__main__":
    link_to_mp3(sys.argv[1])
    filename += ".mp3"
    generate_text("result/{0}".format(filename))
    filename = filename.replace(".mp3", ".txt")
    txt_preprocessing("result/{0}".format(filename))
