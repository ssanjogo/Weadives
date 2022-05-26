# Imports
import netCDF4 as nc
import numpy as np
import math
import csv
import firebase_admin
import pyrebase


from random import randint
from firebase_admin import credentials, initialize_app
from firebase_admin import firestore

# PRUEBA 1
# Setup
cred = credentials.Certificate("serviceAccountKey.json")
firebase_admin.initialize_app(cred)

db = firestore.client()

# PRUEBA 2
# Init firebase with your credentials
'''
cred = credentials.Certificate("weadives-f5d6d9621628.json")
initialize_app(cred, {'storageBucket': 'INTRODUCIR BUCKET CODE DE FIREBASE DATABASE'})

# Put your local file path
fileName = "HS.csv"
bucket = storage.bucket()
blob = bucket.blob(fileName)
blob.upload_from_filename(fileName)
'''

# PRUBEA 3
config = {
    "apiKey": "AIzaSyCJqab-HLmlYmf7mjCc6D1xtGD-bm2FAJk",
    "authDomain": "weadives.firebaseapp.com",
    "projectId": "weadives",
    "storageBucket": "weadives.appspot.com",
    "serviceAccount": "serviceAccountKey.json",
    "databaseURL": "weadives.appspot.com"
}

firebase_storage = pyrebase.initialize_app(config)
storage = firebase_storage.storage()

_arr = np.zeros(10, dtype=float)
_arr_ = []
_arr_.append(6.23)
print(type(_arr[0]))
print(type(_arr_[0]))

db.collection('test').document('test_').set({'nose':_arr_})


'''CHULETA TO CREATE COLLECTION/DOCUMENT/SUBSECTIONS
data2={'father':'Frank','mother':'Anna'}
data1={'name':'Mike','age':40,'parents':data2}
db.collection('test01').document('test02').set(data1)
'''

# Routes
PSL_r = r'wetransfer_datos_maldivas_2021-05-13-tar-gz_2022-03-02_1550/Datos_Maldivas_2021-05-13/UIB_PSL_'
U10MET_r = r'wetransfer_datos_maldivas_2021-05-13-tar-gz_2022-03-02_1550/Datos_Maldivas_2021-05-13/UIB_U10MET_'
V10MET_r = r'wetransfer_datos_maldivas_2021-05-13-tar-gz_2022-03-02_1550/Datos_Maldivas_2021-05-13/UIB_V10MET_'
T2_r = r'wetransfer_datos_maldivas_2021-05-13-tar-gz_2022-03-02_1550/Datos_Maldivas_2021-05-13/UIB_T2_'
Oleaje_r = r'wetransfer_datos_maldivas_2021-05-13-tar-gz_2022-03-02_1550\Oleaje_espectral\schout_'

sufix = '.nc'

storage_r = 'Weather_data/Coord_data/'

# RANGED DICTIONARY
class RangeDict(dict):
    def __getitem__(self, item):
        if not isinstance(item, range):  # or xrange in Python 2
            for key in self:
                if item in key:
                    return self[key]
            raise KeyError(item)
        else:
            return super().__getitem__(item)  # or super(RangeDict, self) for Python 2


DegreeToDirection = RangeDict({range(0, 23): 'N', range(23, 68): 'NE', range(68, 113): 'E',
                               range(113, 158): 'SE', range(158, 203): 'S', range(203, 248): 'SO',
                               range(248, 293): 'O', range(293, 338): 'NO', range(338, 361): 'N',
                               range(400, 500): 'X'})


# GET WEATHER POSITIONS DATA
def getWeatherPosdata():
    PSL_data = nc.Dataset(PSL_r + '2021-05-12' + sufix)

    lat = PSL_data['lat'][:]
    lon = PSL_data['lon'][:]

    return lat.astype(np.float64), lon.astype(np.float64)


# GET PSL DATA
def getPSLdata(date):
    PSL_data = nc.Dataset(PSL_r + date + sufix)

    lat = PSL_data['lat'][:]
    lon = PSL_data['lon'][:]
    time = PSL_data['time'][:]
    PSL = PSL_data['PSL'][:]

    return PSL.astype(np.float64)


# GET WIND DATA
def getWINDdata(date):
    print('START GETTING WIND DATA')
    U10_data = nc.Dataset(U10MET_r + date + sufix)
    V10_data = nc.Dataset(V10MET_r + date + sufix)

    U10 = U10_data['U10MET'][:]
    V10 = V10_data['V10MET'][:]



    WIND_D = np.empty([24, 531, 450])

    print('GETTING WIND DIRECTION')

    R10 = np.empty([24, 531, 450])
    for t in range(24):
        R10[t, :, :] = np.arccos(V10[t, :, :]/np.hypot(U10[t, :, :], V10[t, :, :])).astype(int)

    for t in range(24):
        for lat in range(531):
            for lon in range(450):
                WIND_D[t, lat, lon] = calc_vectores(U10[t, lat, lon], V10[t, lat, lon], R10[t, lat, lon])
        print(t)
    print('GOT WIND DIRECTION')

    print('GETTING WIND VALUES')
    for t in range(24):
        R10[t, :, :] = np.hypot(U10[t, :, :], V10[t, :, :]) * 1.94384
    print('GOT WIND VALUES')

    return R10.astype(np.float64), WIND_D.astype(np.float64)

#
# A = posR
#
def get_angle(A):
    magnitudA = np.sqrt(((A[0] ** 2) + (A[1] ** 2)))

    cos = math.fabs(A[1] / magnitudA)
    val = math.acos(cos)

    return round(math.degrees(val))


# GET TEMPERATURE DATA
def getT2data(date):
    T2_data = nc.Dataset(T2_r + date + sufix)

    T2 = T2_data['T2'][:] - 273.15

    return T2.astype(np.float64)


# GET WAVES POSITIONS DATA
def getWavesPosdata():
    WAVES_data = nc.Dataset(Oleaje_r + '1' + sufix)

    lon_waves = WAVES_data['SCHISM_hgrid_node_x'][:]
    lat_waves = WAVES_data['SCHISM_hgrid_node_y'][:]

    return lat_waves.astype(np.float64), lon_waves.astype(np.float64)


# GET WAVES DATA
def getWAVESdata(date):
    WAVES_data = nc.Dataset(Oleaje_r + date + sufix)

    HS = WAVES_data['WWM_1'][:]
    TP = WAVES_data['WWM_11'][:]
    DP = WAVES_data['WWM_18'][:]

    return HS.astype(np.float64), TP.astype(np.float64), DP.astype(np.int)


'''
#
#   U10: (x)    V10: (y)
#   W -> E      S -> N
#   -    +      -    +
#
'''


def calc_vectores(U10, V10, R10):
    # EN CAS QUE ALGUN DELS VALORS SIGUI 0
    if zer(U10) and zer(V10):
        return 420

    elif zer(U10) and not zer(V10):
        if (pos(V10)):
            return 0
        else:
            return 180
    elif not zer(U10) and zer(V10):
        if (pos(U10)):
            return 90
        else:
            return 270

    # SABEM QUE ELS DOS VALORS NO SON 0

    if pos(U10) and pos(V10):
        return R10

    elif pos(U10) and not pos(V10):
        return 180 - R10

    elif not pos(U10) and not pos(V10):
        return 180 + R10

    elif not pos(U10) and pos(V10):
        return (180 - R10) + 180


def zer(_val):
    return _val == 0


def pos(_val):
    return _val > 0




# Testeo
'''
date = '2021-05-'
day = 12
date = date + str(day)
lat, lon, time, PSL = getPSLdata(date)
WIND, WIND_D = getWINDdata(date)
T2 = getT2data(date)
W_lat, W_lon, HS, TP, DP = getWAVESdata(str(3))
'''


def percentage(part, whole):
    percentage = 100 * float(part) / float(whole)
    return str(percentage) + "%"


def convert_pos(pts, pt):
    x, y = pt
    index = -1
    smallest = float("inf")
    for p in pts:
        dist = abs(x - p[0]) + abs(y - p[1])
        if dist < smallest:
            index = pts.index(p)
            smallest = dist

    return index


def create_csv():
    
    print(storage.child('Weather_data').child('Coord_data').child('-0.0541915894_65.4908447266.csv').get_url('5bb5f2af-89bf-4114-a1b9-f806fe135c0d'))
    exit(2)

    header = np.empty([73], dtype='U25')
    header[0] = '**'
    divisor = 3
    i = 1

    for day in range(3):
        for t in range(1, 25):
            header[i] = str(day) + '_' + str(t)
            i += 1

    W_lat, W_lon = getWavesPosdata()
    lat, lon = getWeatherPosdata()

    lat_data = []
    lon_data = []


    i = 0

    for _lat in range(0, lat.shape[0], divisor):  # lat.shape[0]
        #lat_data[i] = round(lat[_lat, 0], 10)
        lat_data.append(round(lat[_lat, 0], 10))
        i += 1

    db.collection('Data').document('Coord_base').set({'Lat': lat_data}, merge = True)

    i = 0

    for _lon in range(0, lon.shape[1], divisor):  # lon.shape[1]
        #lon_data[i] = str(round(lon[0, _lon], 10))
        lon_data.append(round(lon[0, _lon], 10))
        i += 1

    db.collection('Data').document('Coord_base').set({'Lon': lon_data}, merge = True)


    exit(1)

    W_points = []

    for i in range(W_lat.shape[0]):
        W_points.append((W_lat[i],W_lon[i]))

    for i in range(10):
        _lat = randint(0,300)
        _lon = randint(0,300)
        _pos = convert_pos(W_points, (lat[_lat,0],lon[0,_lon]))

        print('Weather_pos[' + str(_lat) + ',' + str(_lon) + ']:', lat[_lat,0], ',', lon[0,_lon], '| ' +
              'Waves_pos[' + str(_pos) + ']:', W_lat[_pos], ',', W_lon[_pos])


    # Importamos todos los ficheros de datos para posteriormente tratar con ellos.
    año = 2021
    mes = 5
    dia = 12

    date = str(año) + '-0' + str(mes) + '-' + str(dia)
    # Get PSL data + lat | lon
    PSL_1 = getPSLdata(date)
    # Get WIND data
    WIND_1, WIND_D_1 = getWINDdata(date)
    # Get T2 data
    T2_1 = getT2data(date)
    # Get WAVES data
    HS_1, TP_1, DP_1 = getWAVESdata('1')
    dia += 1

    date = str(año) + '-0' + str(mes) + '-' + str(dia)
    # Get PSL data + lat | lon
    PSL_2 = getPSLdata(date)
    # Get WIND data
    WIND_2, WIND_D_2 = getWINDdata(date)
    # Get T2 data
    T2_2 = getT2data(date)
    # Get WAVES data
    HS_2, TP_2, DP_2 = getWAVESdata('2')
    dia += 1

    date = str(año) + '-0' + str(mes) + '-' + str(dia)
    # Get PSL data + lat | lon
    PSL_3 = getPSLdata(date)
    # Get WIND data
    WIND_3, WIND_D_3 = getWINDdata(date)
    # Get T2 data
    T2_3 = getT2data(date)
    # Get WAVES data
    HS_3, TP_3, DP_3 = getWAVESdata('3')

    HS_data = np.empty([73], dtype='U25')
    DP_data = np.empty([73], dtype='U25')
    TP_data = np.empty([73], dtype='U25')
    PSL_data = np.empty([73], dtype='U25')
    T2_data = np.empty([73], dtype='U25')
    WIND_D_data = np.empty([73], dtype='U25')
    WIND_data = np.empty([73], dtype='U25')

    HS_data[0] = "HS (m)"
    DP_data[0] = "DP"
    TP_data[0] = "TP (s)"
    PSL_data[0] = "PSL (hPa)"
    T2_data[0] = "T2 (ªC)"
    WIND_D_data[0] = "WIND_D"
    WIND_data[0] = "WIND (kt)"



    lat_data = np.empty([lat.shape[0]//divisor])
    lon_data = np.empty([lat.shape[0] // divisor])




    for _lat in range(0, lat.shape[0], divisor):  # lat.shape[0]
        for _lon in range(0, lon.shape[1], divisor):  # lon.shape[1]

            _pos = convert_pos(W_points, (lat[_lat,0], lon[0,_lon]))
            i = 1
            print('weather_POS[' + str(lat[_lat,0]) + ',' + str(lon[0,_lon]) + ']:', _lat, ',', _lon, ' || waves_value[' + str(_pos) + ']:',
                  W_lat[_pos], ',', W_lon[_pos])

            for day in range(3):

                for t in range(24):
                    if day == 0:
                        if (t < PSL_1.shape[0]):
                            PSL_data[i] = str(round(PSL_1[t, _lat, _lon], 2))
                            WIND_D_data[i] = DegreeToDirection[WIND_D_1[t, _lat, _lon]]
                            WIND_data[i] = str(round(WIND_1[t, _lat, _lon], 1))
                            T2_data[i] = str(round(T2_1[t, _lat, _lon], 1))
                        else:
                            PSL_data[i] = 'X'
                            WIND_D_data[i] = 'X'
                            WIND_data[i] = 'X'
                            T2_data[i] = 'X'

                        if (t < HS_1.shape[0]):
                            HS_data[i] = str(round(HS_1[t, _pos],1))
                            DP_data[i] = DegreeToDirection[DP_1[t, _pos]]
                            TP_data[i] = str(round(TP_1[t, _pos],1))
                        else:
                            HS_data[i] = 'X'
                            DP_data[i] = 'X'
                            TP_data[i] = 'X'


                    if day == 1:
                        if (t < PSL_2.shape[0]):
                            PSL_data[i] = str(round(PSL_2[t, _lat, _lon], 2))
                            WIND_D_data[i] = DegreeToDirection[WIND_D_2[t, _lat, _lon]]
                            WIND_data[i] = str(round(WIND_2[t, _lat, _lon], 1))
                            T2_data[i] = str(round(T2_2[t, _lat, _lon], 1))
                        else:
                            PSL_data[i] = 'X'
                            WIND_D_data[i] = 'X'
                            WIND_data[i] = 'X'
                            T2_data[i] = 'X'

                        if (t < HS_2.shape[0]):
                            HS_data[i] = str(round(HS_2[t, _pos],1))
                            DP_data[i] = DegreeToDirection[DP_2[t, _pos]]
                            TP_data[i] = str(round(TP_2[t, _pos],1))
                        else:
                            HS_data[i] = 'X'
                            DP_data[i] = 'X'
                            TP_data[i] = 'X'


                    if day == 2:
                        if (t < PSL_3.shape[0]):
                            PSL_data[i] = str(round(PSL_3[t, _lat, _lon], 2))
                            WIND_D_data[i] = DegreeToDirection[WIND_D_3[t, _lat, _lon]]
                            WIND_data[i] = str(round(WIND_3[t, _lat, _lon], 1))
                            T2_data[i] = str(round(T2_3[t, _lat, _lon], 1))
                        else:
                            PSL_data[i] = 'X'
                            WIND_D_data[i] = 'X'
                            WIND_data[i] = 'X'
                            T2_data[i] = 'X'

                        if(t < HS_3.shape[0]):
                            HS_data[i] = str(round(HS_3[t, _pos],1))
                            DP_data[i] = DegreeToDirection[DP_3[t, _pos]]
                            TP_data[i] = str(round(TP_3[t, _pos],1))
                        else:
                            HS_data[i] = 'X'
                            DP_data[i] = 'X'
                            TP_data[i] = 'X'

                    i += 1

            with open('CSV_files/buff.csv','w', encoding='UTF8', newline='') as f:
                writer = csv.writer(f)

                writer.writerow(header)
                writer.writerow(HS_data)
                writer.writerow(DP_data)
                writer.writerow(TP_data)
                writer.writerow(PSL_data)
                writer.writerow(T2_data)
                writer.writerow(WIND_D_data)
                writer.writerow(WIND_data)

            file_r = str(round(lat[_lat, _lon], 10)) + '_' + str(round(lon[_lat, _lon], 10)) + '.csv'
            storage.child(storage_r + file_r).put("CSV_files/buff.csv", '1')


create_csv()

'''
#### LOAD ALL DATA ####
Data to load:
-Wind value
-Wind direction
-PSL
-HS
-TP
-DP
-T2

Data loaded:
-Wind value
-PSL
-T2
'''


def create_data():
    # WEATHER DATA
    dict_type = {0: 'LAT', 1: 'LON', 2: 'PSL', 3: 'WIND_D', 4: 'WIND', 5: 'T2'}
    lat, lon, time, PSL = getPSLdata('2021-05-12')

    divisor = 3

    lat_data = np.empty([lat.shape[0] // divisor, 1])
    lon_data = np.empty([lon.shape[1] // divisor, 1])

    for i in range(0, lat.shape[0], divisor):
        lat_data[i // divisor, 0] = lat[i, 0]

    for i in range(0, lon.shape[1], divisor):
        lon_data[i // divisor, 0] = lon[0, i]

    for i in range(2):
        with open('CSV_files/Weather_data/' + dict_type[i] + '.csv', 'w', encoding='UTF8', newline='') as f:
            writer = csv.writer(f)

            if i == 0:
                writer.writerows(lat_data)
            if i == 1:
                writer.writerows(lon_data)

    # DIA
    for day in range(12, 15):
        print('start day', day)

        date = '2021-05-'
        date = date + str(day)
        print('PSL_data')
        lat, lon, time, PSL = getPSLdata(date)
        print('got PSL_data')
        print('WIND_data')
        WIND, WIND_D = getWINDdata(date)
        print('got WIND_data')
        print('got T2_data')
        T2 = getT2data(date)
        print('T2_data')

        PSL_data = np.empty([lat.shape[0] // divisor, lon.shape[1] // divisor])
        WIND_DIR_data = np.empty([lat.shape[0] // divisor, lon.shape[1] // divisor], dtype=str)
        WIND_data = np.empty([lat.shape[0] // divisor, lon.shape[1] // divisor])
        T2_data = np.empty([lat.shape[0] // divisor, lon.shape[1] // divisor])

        # HORA
        for t in range(0, PSL.shape[0], 2):
            print('hora', t)
            # COORDENADA
            for _lat in range(0, lat.shape[0], divisor):
                for _lon in range(0, lon.shape[1], divisor):
                    PSL_data[_lat // divisor, _lon // divisor] = PSL[t, _lat, _lon]
                    WIND_DIR_data[_lat // divisor, _lon // divisor] = DegreeToDirection[WIND_D[t, _lat, _lon]]
                    WIND_data[_lat // divisor, _lon // divisor] = WIND[t, _lat, _lon]
                    T2_data[_lat // divisor, _lon // divisor] = T2[t, _lat, _lon]
                print('lat', _lat)

            print('END_A', day, t)

            # TIPO DE DATA
            for i in range(2, 6):

                with open('CSV_files/Weather_data/2021-05-' + str(day) + '/' + dict_type[i] + '_' + str(t) + '.csv',
                          'w', encoding='UTF8', newline='') as f:
                    writer = csv.writer(f)

                    if i == 2:
                        writer.writerows(PSL_data)
                    elif i == 3:
                        writer.writerows(WIND_DIR_data)
                    elif i == 4:
                        writer.writerows(WIND_data)
                    elif i == 5:
                        writer.writerows(T2_data)

            print('END_B', day, t)

    print('DONEE')
    exit(1)

    # WAVE DATA
    dict_type = {0: 'HS', 1: 'TP', 2: 'DP', 3: 'LAT', 4: 'LON'}
    W_lat, W_lon, HS, TP, DP = getWAVESdata('1')

    lat_data = np.empty([W_lat.shape[0], 1])
    lon_data = np.empty([W_lat.shape[0], 1])

    lat_header = ['latitud']
    lon_header = ['longitud']

    for i in range(0, W_lat.shape[0]):
        lat_data[i, 0] = round(W_lat[i], 9)
        lon_data[i, 0] = round(W_lon[i], 9)

    for i in range(3, 5):
        with open('CSV_files/Waves_data/' + dict_type[i] + '.csv', 'w', encoding='UTF8', newline='') as f:
            writer = csv.writer(f)

            if i == 3:
                writer.writerow(lat_header)
                writer.writerows(lat_data)
            if i == 4:
                writer.writerow(lon_header)
                writer.writerows(lon_data)

    for day in range(1, 4):
        W_lat, W_lon, HS, TP, DP = getWAVESdata(str(day))

        size = math.ceil(HS.shape[0] / 2)

        header = [0] * size

        HS_data = np.empty([W_lat.shape[0], size])
        TP_data = np.empty([W_lat.shape[0], size])
        DP_data = np.empty([W_lat.shape[0], size], dtype=str)

        for t in range(0, (HS.shape[0]), 2):
            for xy in range(0, W_lat.shape[0]):
                HS_data[xy, t // 2] = HS[t, xy]
                TP_data[xy, t // 2] = TP[t, xy]

                temp_dp = DP[t, xy]
                final_dp = temp_dp - 180 if (temp_dp - 180) > 0 else temp_dp + 180
                DP_data[xy, t // 2] = DegreeToDirection[round(final_dp)]
            print('A:', day, t)

        for i in range(3):
            for t in range(0, (HS.shape[0]), 2):
                header[t // 2] = dict_type[i] + '_' + str(t)

            with open('CSV_files/Waves_data/2021-05-' + str(11 + day) + '/' + dict_type[i] + '.csv', 'w',
                      encoding='UTF8', newline='') as f:
                writer = csv.writer(f)

                writer.writerow(header)
                if (i == 0):
                    writer.writerows(HS_data)
                if (i == 1):
                    writer.writerows(TP_data)
                if (i == 2):
                    writer.writerows(DP_data)

            print('B:', day, t, i)
        print('DONE', day)


def load_data():
    for i in range(3):
        date = '2021-05-'
        day = 12 + i
        date = date + str(day)
        lat, lon, time, PSL = getPSLdata(date)
        WIND, WIND_D = getWINDdata(date)
        T2 = getT2data(date)
        W_lat, W_lon, HS, TP, DP = getWAVESdata(str(i + 1))

        print('i:', i)
        print('Starting to load Wave data...')
        for t in range(0, 24, 2):
            for xy in range(0, W_lat.shape[0]):
                temp_dp = DP[t, xy]
                final_dp = temp_dp - 180 if (temp_dp - 180) > 0 else temp_dp + 180
                waves_data = {'HS': HS[t, xy], 'TP': TP[t, xy], 'DP': DegreeToDirection[round(final_dp)]}
                w_coord_data = {(str(W_lat[xy]) + '|' + str(W_lon[xy])): waves_data}
                #db.collection('Weather').document('Waves_Data').collection(date).document(str(t)).set(w_coord_data, merge=True)
                print('xy:', xy, ' | ', percentage(xy, W_lat.shape[0]), 'from the Wave\'s part')
            print('t:', t, ' | ', percentage((t / 2), 12, 'from the Whole Wave\'s part'), ' || ', percentage((t / 2), 24), 'from the Whole part')

        for t in range(0, 24, 2):
            for x in range(0, lat.shape[0]):
                for y in range(0, lat.shape[1]):
                    print(t, lat[x, 0], lon[0, y])
                    weather_data = {'PSL': PSL[t, x, y], 'WIND': WIND[t, x, y], 'T2': T2[t, x, y]}
                    coord_data = {(str(lat[x, 0]) + '|' + str(lon[0, y])): weather_data}
                    #db.collection('Weather').document('Weather_Data').collection(date).document(str(t)).set(coord_data, merge=True)
                print('x', x, ' | ', percentage(x, lat.shape[0]), 'from the Weather\'s part')
            print('t:', t, ' | ', percentage((t / 2), 12, 'from the Whole Weather\'s part'), ' || ', percentage((t / 2), 24), 'from the Whole part')


'''CHULETA TO CREATE COLLECTION/DOCUMENT/SUBSECTIONS
data2={'father':'Frank','mother':'Anna'}
data1={'name':'Mike','age':40,'parents':data2}
db.collection('test01').document('test02').set(data1)
'''