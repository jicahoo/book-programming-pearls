/*
 * C Program to Find the Number of Lines in a Text File
 */
#include <stdio.h>
#include <malloc.h>
#include <pthread.h>
//#include<string.h>

void *count_lines(void *vargp) {
    int size = 11*100*1000*1000;
    printf("before str[size]\n");
    char* str = malloc(sizeof(char)*(size+1));
    printf("before line[11]\n");
    char line[11] = {'0','1','2','3','4','5','6','7','8','9','\n'};
    printf("before first loop\n");
    for (int i = 0; i < size; i++) {

       int idx = i % 11;
       char c =line[idx];
       str[i] = c;
    }
    int lineCnt = 0;
    for (int i = 0; i < size; i++) {
       if (str[i] == '\n') {
          lineCnt++;
       }
    }
    printf("There are %d lines\n", lineCnt);
}

int main()
{
    /*
     * Two threads make fast.
     * real	0m5.105s
     * user	0m9.523s
     * sys	0m0.560s
    */
    pthread_t thread_a;
    pthread_t thread_b;
    pthread_create(&thread_a, NULL, count_lines, NULL);
    pthread_create(&thread_b, NULL, count_lines, NULL);
    pthread_join(thread_a, NULL);
    pthread_join(thread_b, NULL);
    return 0;
}
