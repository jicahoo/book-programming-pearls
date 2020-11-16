#include <fcntl.h>
#include <stdio.h>
#include <unistd.h>
#include <errno.h>
#include <sys/time.h>


void handle_data(char* buf, ssize_t size) {
    printf("Handle data: %ld\n", size);
}
int main() {
    char buf[1024*10] = {0};
    //struct timespec sleep_interval= {0, 1000};
    struct timespec sleep_interval = {.tv_sec = 0, .tv_nsec = 1000};

    ssize_t nbytes;
    //FILE* fStream = fopen("input.txt", "r");
    int fd = STDIN_FILENO;

    int flags = fcntl(fd, F_GETFL, 0);
    fcntl(fd, F_SETFL, flags | O_NONBLOCK);

    for (;;) {
        /* try fd1 */
        if ((nbytes = read(fd, buf, sizeof(buf))) < 0) {
            if (errno != EWOULDBLOCK) {
                perror("read/fd1");
            }
            printf("Not got data!");
        } else {
            handle_data(buf, nbytes);
        }

        /* sleep for a bit; real version needs error checking! */
        //nanosleep(sleep_interval, NULL);
        sleep(2);

    }
    return 0;
}
