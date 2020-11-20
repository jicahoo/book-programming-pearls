#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>

#define MAX_INT 0x7fffffff
#define MAX_LINE_LENGTH 80

char* double_to_string(double d) {
    size_t len;
    char *str;

    len = (size_t)snprintf(NULL, 0, "%f", d) + 1;
    str = calloc(len, sizeof(char));
    snprintf(str, len, "%f", d);
    return str;
}

void write_to(FILE* f, int i) {
    int length = snprintf(NULL, 0, "%d", i);
    char* str = calloc( length + 1, sizeof(char));
    snprintf( str, length + 1, "%d", i);
    fputs(str, f);
    fputs("\n", f);
    free(str);
}

int find_missing_int(char* file_path, int firstIdx, int lastIdx) {
    double mid = (firstIdx + lastIdx)/2.0;
    double halfCnt = (lastIdx - firstIdx)/2.0;

    int midIsInt = 0;
    int midInt = 0;
    if (floor(mid) == mid) {
        midIsInt = 1;
        midInt = (int)mid;
    }

    int midIntFound = 0;
    int lessCnt = 0;
    char* double_str = double_to_string(mid);
    size_t d_str_len = strlen(double_str);
    size_t less_str_len = strlen("less-");

    char* lessFilePath = calloc( (d_str_len + less_str_len + 1), sizeof(char));
    strcat(lessFilePath, "less-");
    strcat(lessFilePath, double_str);

    printf("less file path: %s\n", lessFilePath);
    FILE* lessFile = fopen(lessFilePath, "w+");
    int greaterCnt = 0;
    size_t greater_str_len = strlen("greater-");

    char* greaterFilePath = calloc((d_str_len + greater_str_len + 1), sizeof(char));
    strcat(greaterFilePath, "greater-");
    strcat(greaterFilePath, double_str);
    printf("greater file path: %s\n", greaterFilePath);

    FILE* greaterFile = fopen(greaterFilePath, "w+");
    free(double_str);

    FILE* file = fopen(file_path, "r");
    char line[MAX_LINE_LENGTH] = {0};

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

int main() {
    printf("Hello World!\n");
    char* file_path = "input.txt";
    //TODO: iterative version
    int missed = find_missing_int(file_path, 0, MAX_INT);
    printf("Found missing number: %d\n", missed);
    return 0;
}