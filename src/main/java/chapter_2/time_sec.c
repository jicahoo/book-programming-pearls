#include <stdio.h>
#include <time.h>
#include <math.h>
#include <unistd.h>

long get_duration(struct timespec *start, struct timespec* end) {
    long TO_NSEC = (long)pow(10, 9);
    long s_nsec = start->tv_sec * TO_NSEC + start->tv_nsec;
    long e_nsec = end->tv_sec * TO_NSEC + end->tv_nsec;
    long d_nsec = e_nsec - s_nsec;
    return d_nsec;
}
 
int main(void)
{
    struct timespec ts;
    timespec_get(&ts, TIME_UTC);
    sleep(2);
    char buff[100];
    strftime(buff, sizeof buff, "%D %T", gmtime(&ts.tv_sec));
    //printf("Current time: %s.%09ld UTC\n", buff, ts.tv_nsec);
    struct timespec end;
    timespec_get(&end, TIME_UTC);
    long d_nsec = get_duration(&ts, &end);
    printf("Duration: %ld\n",  d_nsec);
}
