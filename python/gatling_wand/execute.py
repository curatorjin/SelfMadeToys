import yaml
import time
import threading


def thread_execute(func, wait=0, loop=1):
    t = threading.Thread(target=processing, args=(func, wait, loop), daemon=False)
    t.start()


def processing(func, wait, loop):
    if func:
        if loop == 0:
            while True:
                time.sleep(wait)
                func()
        else:
            for x in range(0, loop):
                time.sleep(wait)
                func()


def main():
    with open('config.yml', encoding='utf-8') as f:
        task_data = yaml.load(f.read(), Loader=yaml.FullLoader)
        for key in task_data:
            dd = __import__('task.' + key, fromlist='True')
            f = getattr(dd, 'run')
            thread_execute(f, task_data[key]['wait'], task_data[key]['loop'])


if __name__ == '__main__':
    main()
