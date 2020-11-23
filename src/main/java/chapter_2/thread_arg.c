#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>

typedef struct args {
    char* name;
    int age;
} args_t;

void *hello(void *input) {
    printf("name: %s\n", ((args_t*)input)->name);
    printf("age: %d\n", ((args_t*)input)->age);
}

int main() {
    args_t *Allen = (args_t*)malloc(sizeof(args_t));
    char allen[] = "Allen";
    Allen->name = allen;
    Allen->age = 20;

    pthread_t tid;
    pthread_create(&tid, NULL, hello, (void *)Allen);
    pthread_join(tid, NULL);
    return 0;
}
