#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

// fseek ftell.
//https://www.quora.com/How-can-I-read-a-file-form-middle-in-C-For-exam-we-can-give-position-to-fseek-method-in-the-same-way-how-can-we-read-file-from-a-particular-position

/*

handle a file about 40G and 4*10^9 lines.

time result:

real	7m24.099s
user	11m12.078s
sys	3m33.609s

*/
typedef struct FileRange {
    FILE *fp;
    long int firstIdx;
    long int lastIdx;
} FileRangeType;

long int count_lines(FILE *fp, long int firstIdx, long int lastIdx) {
    char c;
    long int cnt = 0;
    for (long int i = firstIdx; i <= lastIdx; i++) {
        c = getc(fp);
        //printf("Got char: %c\n", c);
        if (c == '\n') {
            cnt++;
        }
    }
    return cnt;
}

long int count_lines_for_thread(void *args) {
    FileRangeType* fileRange = ((FileRangeType*)args);
    FILE *fp = fileRange->fp;
    long int firstIdx = fileRange->firstIdx;
    long int lastIdx = fileRange->lastIdx;
    free(fileRange);
    return count_lines(fp, firstIdx, lastIdx);
}

void *thread_callback_count_lines(void *args) {
    long int cnt = count_lines_for_thread(args);
    printf("Count: %ld\n", cnt);
}


void first_draft(){
    printf("Hello World!\n");
    long int pos;
    FILE *fp = fopen("input.txt", "r");
    long int startPos = ftell(fp);
    printf("The startPos: %ld\n", startPos);

    /* Position the stream to the end of fyle and get the byte offset. */
    fseek(fp,0, SEEK_END);
    pos = ftell(fp);
    printf("pos: %ld\n", pos);

    /* Do the math. */
    long int mid = pos / 2;
    printf("The middle of the file is at %ld bytes from the start.\n", mid);

    /* Position stream at the middle. */
    fseek(fp, mid, SEEK_SET);
    long int tellPos = ftell(fp);
    printf("The pos: %ld\n", tellPos);
}

int main() {
    char filePath[] = "input.txt";
    FILE *fpFirst = fopen(filePath, "r");
    FILE *fpSecond = fopen(filePath, "r");
    fseek(fpFirst,0, SEEK_END);
    long int first = 1L;
    long int last = ftell(fpFirst);
    //printf("last: %ld\n", last);
    long int mid = last / 2;

    FileRangeType* fileRangeFirst = (FileRangeType *)malloc(sizeof(FileRangeType));
    fileRangeFirst->fp = fpFirst;
    fileRangeFirst->firstIdx = first;
    fileRangeFirst->lastIdx = mid;
    
    //long int firstPartLines = count_lines_for_thread(fileRange);
    fseek(fpFirst, 0, SEEK_SET);
    pthread_t tidFirst;
    pthread_create(&tidFirst, NULL, thread_callback_count_lines, (void *)fileRangeFirst);

    //long int firstPartLines = count_lines_for_thread(fileRangeFirst);
    //printf("First part line number is %ld\n", firstPartLines);

    FileRangeType* fileRangeSecond = (FileRangeType *)malloc(sizeof(FileRangeType));
    fileRangeSecond->fp = fpSecond;
    fileRangeSecond->firstIdx = mid + 1;
    fileRangeSecond->lastIdx = last;
    fseek(fpSecond, mid, SEEK_SET);

    pthread_t tidSecond;
    pthread_create(&tidSecond, NULL, thread_callback_count_lines, (void *)fileRangeSecond);

    //long int secondPartLines = count_lines_for_thread(fileRangeSecond);
    //printf("Second part line number is %ld\n", secondPartLines);

    pthread_join(tidSecond, NULL);
    pthread_join(tidFirst, NULL);

    fclose(fpFirst);
    fclose(fpSecond);

    return 0;
}
