#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include <time.h>

#define MAX_INT 0x7fffffff
#define MAX_LINE_LENGTH 80

int find_missing_int_call_cnt = 0;
int tail_cnt = 0;
long avg_nsecs = 0;
long avg_nsecs_sec = 0;
long avg_nsecs_third = 0;

long get_duration(struct timespec *start, struct timespec* end) {
    long TO_NSEC = (long)pow(10, 9);
    long s_nsec = start->tv_sec * TO_NSEC + start->tv_nsec;
    long e_nsec = end->tv_sec * TO_NSEC + end->tv_nsec;
    long d_nsec = e_nsec - s_nsec;
    return d_nsec;
}

char* double_to_string(double d) {
    size_t len;
    char *str;

    len = (size_t)snprintf(NULL, 0, "%f", d) + 1;
    //printf("got double space require: %lu\n", len);
    str = calloc(len, sizeof(char));
    snprintf(str, len, "%f", d);
    return str;
}

void write_to(FILE* f, int i) {
    int length = snprintf(NULL, 0, "%d", i);
    //printf("write_to: got double space require: %d\n", length);
    char* str = calloc( length + 1, sizeof(char));
    snprintf( str, length + 1, "%d", i);
    fputs(str, f);
    fputs("\n", f);
    free(str);
}

void set_buf_size(FILE* fp) {
    char* buf = calloc(8192, sizeof(char));
    if(setvbuf(fp, buf, _IOFBF, 8192) != 0) {
       perror("setvbuf failed"); // POSIX version sets errno
    }

}

int find_missing_int(char* file_path, int firstIdx, int lastIdx) {
    find_missing_int_call_cnt++;
    double mid = (firstIdx + lastIdx)/2.0; double halfCnt = (lastIdx - firstIdx)/2.0; 
    int midIsInt = 0;
    int midInt = 0;
    if (floor(mid) == mid) {
        midIsInt = 1;
        midInt = (int)mid;
    }

    int midIntFound = 0;
    char* double_str = double_to_string(mid);
    size_t d_str_len = strlen(double_str);
    // printf("double len: %lu\n", d_str_len);

    int lessCnt = 0;
    size_t less_str_len = strlen("less-");
    char* lessFilePath = calloc( (d_str_len + less_str_len + 1), sizeof(char));
    strcat(lessFilePath, "less-");
    strcat(lessFilePath, double_str);
    //printf("less file path: %s\n", lessFilePath);
    FILE* lessFile = fopen(lessFilePath, "w+");

    int greaterCnt = 0;
    size_t greater_str_len = strlen("greater-");
    char* greaterFilePath = calloc((d_str_len + greater_str_len + 1), sizeof(char));
    strcat(greaterFilePath, "greater-");
    strcat(greaterFilePath, double_str);
    //printf("greater file path: %s\n", greaterFilePath);
    FILE* greaterFile = fopen(greaterFilePath, "w+");

    // free(double_str);

    FILE* file = fopen(file_path, "r");
    char line[MAX_LINE_LENGTH] = {0};
    set_buf_size(file);

    //Analyze input and record
    while (fgets(line, MAX_LINE_LENGTH, file))
    {
        if (strlen(line) > 1) {
            line[strlen(line)-1]  = '\0';
            int i = atoi(line);

            if (midIsInt) {

                if (i == midInt) {
                    midIntFound = 1;
               } else if (i < midInt) {
                    lessCnt++;
                    write_to(lessFile, i);
                } else {
                    greaterCnt++;
                    write_to(greaterFile, i);
                }
            } else {

                if (i < mid) {
                    lessCnt++;
                    write_to(lessFile, i);
                } else {
                    greaterCnt++;
                    write_to(greaterFile, i);
                }

            }
        }
    }

    //clear resource in time.
    fclose(file);
    //remove file.
    remove(file_path);
    fclose(lessFile);
    fclose(greaterFile);

    //Based on analysis, determine next step: got final result, or search on small data set.
    if (midIsInt) {
        if (!midIntFound) {
            return midIntFound;
        } else {
            if (lessCnt < halfCnt) {
                // remove greater file
                remove(greaterFilePath);
                free(greaterFilePath);
                return find_missing_int(lessFilePath, firstIdx, midInt - 1);
            } else {
                // remove less file
                remove(lessFilePath);
                free(lessFilePath);
                return find_missing_int(greaterFilePath, midInt + 1, lastIdx);
            }
        }
    } else  {
        if (lessCnt < halfCnt) {
            // remove greater file
            remove(greaterFilePath);
            free(greaterFilePath);
            return find_missing_int(lessFilePath, firstIdx, floor(mid));
        } else {
            // remove less file
            remove(lessFilePath);
            free(lessFilePath);
            return find_missing_int(greaterFilePath, ceil(mid), lastIdx);
        }
    }

}

int find_missing_int_v2(char* file_path, int firstIdx, int lastIdx) {
    struct timespec start;
    timespec_get(&start, TIME_UTC);

    find_missing_int_call_cnt++;
    double mid = (firstIdx + lastIdx)/2.0;
    double halfCnt = (lastIdx - firstIdx)/2.0;

    int midIsInt = 0;
    int midInt = 0;
    if (floor(mid) == mid) {
        midIsInt = 1;
        midInt = (int)mid;
    }

    int midIntFound = 0;
    char* double_str = double_to_string(mid);
    size_t d_str_len = strlen(double_str);
    // printf("double len: %lu\n", d_str_len);

    int lessCnt = 0;
    char* lessFilePath = calloc( (d_str_len + 2), sizeof(char));
    strcat(lessFilePath, "l");
    strcat(lessFilePath, double_str);
    //printf("less file path: %s\n", lessFilePath);
    FILE* lessFile = fopen(lessFilePath, "w+");
    set_buf_size(lessFile);

    int greaterCnt = 0;
    char* greaterFilePath = calloc((d_str_len + 2), sizeof(char));
    strcat(greaterFilePath, "g");
    strcat(greaterFilePath, double_str);
    //printf("greater file path: %s\n", greaterFilePath);
    FILE* greaterFile = fopen(greaterFilePath, "w+");
    set_buf_size(greaterFile);

    // free(double_str);

    FILE* file = fopen(file_path, "r");
    char line[MAX_LINE_LENGTH] = {0};
    set_buf_size(file);

    struct timespec end;
    timespec_get(&end, TIME_UTC);
    long dur = get_duration(&start, &end);
    if (avg_nsecs == 0) {
        avg_nsecs = dur;
    } else {
        avg_nsecs = (avg_nsecs + dur)/2;
    }

    //Analyze input and record
    while (fgets(line, MAX_LINE_LENGTH, file))
    {
        if (strlen(line) > 1) {
            line[strlen(line)-1]  = '\0';
            int i = atoi(line);

            if (midIsInt) {

                if (i == midInt) {
                    midIntFound = 1;
               } else if (i < midInt) {
                    lessCnt++;
                    write_to(lessFile, i);
                } else {
                    greaterCnt++;
                    write_to(greaterFile, i);
                }
            } else {

                if (i < mid) {
                    lessCnt++;
                    write_to(lessFile, i);
                } else {
                    greaterCnt++;
                    write_to(greaterFile, i);
                }

            }
        }
    }

    //clear resource in time.
    fclose(file);
    //remove file.
    remove(file_path);
    fclose(lessFile);
    fclose(greaterFile);

    struct timespec three;
    timespec_get(&three, TIME_UTC);
    long dur_sec = get_duration(&end, &three);
    if (avg_nsecs_sec == 0) {
        avg_nsecs_sec = dur_sec;
    } else {
        avg_nsecs_sec = (avg_nsecs_sec + dur_sec)/2;
    }


    //Based on analysis, determine next step: got final result, or search on small data set.
    int result = -1;
    if (midIsInt) {
        if (!midIntFound) {
            result = midIntFound;
        } else {
            if (lessCnt < halfCnt) {
                // remove greater file
                remove(greaterFilePath);
                free(greaterFilePath);
                result = find_missing_int_v2(lessFilePath, firstIdx, midInt - 1);
            } else {
                // remove less file
                remove(lessFilePath);
                free(lessFilePath);
                result = find_missing_int_v2(greaterFilePath, midInt + 1, lastIdx);
            }
        }
    } else  {
        if (lessCnt < halfCnt) {
            // remove greater file
            remove(greaterFilePath);
            free(greaterFilePath);
            result = find_missing_int_v2(lessFilePath, firstIdx, floor(mid));
        } else {
            // remove less file
            remove(lessFilePath);
            free(lessFilePath);
            result = find_missing_int_v2(greaterFilePath, ceil(mid), lastIdx);
        }
    }

    struct timespec four;
    timespec_get(&four, TIME_UTC);
    long dur_third = get_duration(&three, &four);
    if (avg_nsecs_third == 0) {
        avg_nsecs_third = dur_third;
    } else {
        avg_nsecs_third = (avg_nsecs_third + dur_third)/2;
    }
    tail_cnt++;
    return result;

}

int find_missing_int_v3(char* file_path, int firstIdx, int lastIdx) {
    struct timespec start;
    timespec_get(&start, TIME_UTC);

    find_missing_int_call_cnt++;
    double mid = (firstIdx + lastIdx)/2.0;
    double halfCnt = (lastIdx - firstIdx)/2.0;

    int midIsInt = 0;
    int midInt = 0;
    if (floor(mid) == mid) {
        midIsInt = 1;
        midInt = (int)mid;
    }

    int midIntFound = 0;
    char* double_str = double_to_string(mid);
    size_t d_str_len = strlen(double_str);
    // printf("double len: %lu\n", d_str_len);

    int lessCnt = 0;
    char* lessFilePath = calloc( (d_str_len + 2), sizeof(char));
    strcat(lessFilePath, "l");
    strcat(lessFilePath, double_str);
    //printf("less file path: %s\n", lessFilePath);
    FILE* lessFile = fopen(lessFilePath, "w+");
    set_buf_size(lessFile);

    int greaterCnt = 0;
    char* greaterFilePath = calloc((d_str_len + 2), sizeof(char));
    strcat(greaterFilePath, "g");
    strcat(greaterFilePath, double_str);
    //printf("greater file path: %s\n", greaterFilePath);
    FILE* greaterFile = fopen(greaterFilePath, "w+");
    set_buf_size(greaterFile);

    // free(double_str);

    FILE* file = fopen(file_path, "r");
    char line[MAX_LINE_LENGTH] = {0};
    set_buf_size(file);

    struct timespec end;
    timespec_get(&end, TIME_UTC);
    long dur = get_duration(&start, &end);
    if (avg_nsecs == 0) {
        avg_nsecs = dur;
    } else {
        avg_nsecs = (avg_nsecs + dur)/2;
    }

    //Analyze input and record
    while (fgets(line, MAX_LINE_LENGTH, file))
    {
        if (strlen(line) > 1) {
            line[strlen(line)-1]  = '\0';
            int i = atoi(line);

            if (midIsInt) {

                if (i == midInt) {
                    midIntFound = 1;
               } else if (i < midInt) {
                    lessCnt++;
                    write_to(lessFile, i);
                } else {
                    greaterCnt++;
                    write_to(greaterFile, i);
                }
            } else {

                if (i < mid) {
                    lessCnt++;
                    write_to(lessFile, i);
                } else {
                    greaterCnt++;
                    write_to(greaterFile, i);
                }

            }
        }
    }

    //clear resource in time.
    fclose(file);
    //remove file.
    remove(file_path);
    fclose(lessFile);
    fclose(greaterFile);

    struct timespec three;
    timespec_get(&three, TIME_UTC);
    long dur_sec = get_duration(&end, &three);
    if (avg_nsecs_sec == 0) {
        avg_nsecs_sec = dur_sec;
    } else {
        avg_nsecs_sec = (avg_nsecs_sec + dur_sec)/2;
    }


    //Based on analysis, determine next step: got final result, or search on small data set.
    int result = -1;
    char* nextRoundFile;
    int nextStart = 0;
    int nextEnd = 0;
    if (midIsInt & !midIntFound) {
        result = midInt;
    } else {
        if (lessCnt < halfCnt) {
            remove(greaterFilePath);
            free(greaterFilePath);
            nextRoundFile = lessFilePath;
            if (midIsInt) {
                nextStart = firstIdx;
                nextEnd = midIntFound - 1;
            } else {
                nextStart = firstIdx;
                nextEnd = floor(mid);
            }

        } else {
            remove(lessFilePath);
            free(lessFilePath);
            nextRoundFile = greaterFilePath;

            if (midIsInt) {
                nextStart = midInt + 1;
                nextEnd = lastIdx;
            } else {
                nextStart = ceil(mid);
                nextEnd = lastIdx;
            }
        }
        result = find_missing_int_v3(nextRoundFile, nextStart, nextEnd);
    }
    return result;
}

int main() {
/*
Found missing number: 0

real	25m51.346s
user	20m21.796s
sys	5m16.220s

C IO system, architecture

real	0m4.646s
user	0m3.776s
sys	0m0.858s

https://en.cppreference.com/w/c/io/setvbuf


*/
    printf("Hello World!\n");
    char* file_path = "input.txt";
    //TODO: iterative version
    int missed = find_missing_int_v3(file_path, 0, MAX_INT);
    printf("Found missing number: %d\n", missed);
    printf("Call count: %d\n", find_missing_int_call_cnt);
    printf("Tail count: %d\n", tail_cnt);
    printf("avg nano secs: %ld\n", avg_nsecs);
    printf("avg sec nano secs: %ld\n", avg_nsecs_sec);
    printf("avg sec nano secs: %ld\n", avg_nsecs_third);
    return 0;
}
