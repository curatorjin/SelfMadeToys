from selenium import webdriver
from selenium.webdriver import ChromeOptions
import time
import os
import re


def get_driver():
    chrome_options = ChromeOptions()
    chrome_options.add_argument('--headless')
    chrome_options.add_argument('--no-sandbox')
    chrome_options.add_argument('--disable-gpu')
    chrome_options.add_argument('--disable-dev-shm-usage')
    target = webdriver.Chrome(chrome_options=chrome_options)
    return target


def ping_Test(ip):
    ip = ip.replace('\n', '').strip()
    return1 = os.system('ping -c 4 -w 5 %s' % ip)
    if return1:
        print('ping %s is fail' % ip)
        return False
    else:
        print('ping %s is ok' % ip)
        return True


def main():
    url_prefix = 'http://tool.chinaz.com/dns?type=1&host={}&ip='
    driver = get_driver()
    hosts_file = {}
    patten = re.compile('\[[\s\S]*\]')
    with open('data/domain.txt', 'r') as f:
        for line in f.readlines():
            domain = line.strip()
            driver.get(url_prefix.format(domain))
            time.sleep(5)
            dns_list = driver.find_elements_by_xpath(
                '//ul[@class="DnsResuListWrap fl DnsWL"]/li[@class="ReListCent ReLists bor-b1s clearfix"]/div[@class="w60-0 tl"]/p')
            ttl_list = driver.find_elements_by_xpath(
                '//ul[@class="DnsResuListWrap fl DnsWL"]/li[@class="ReListCent ReLists bor-b1s clearfix"]/div[@class="w14-0"]/p')
            target = None
            for i in range(len(dns_list)):
                ip = re.sub(patten, '', dns_list[i].text).strip()
                print(ip)
                if not ping_Test(ip):
                    continue
                if target:
                    target = i if int(ttl_list[target].text) > int(ttl_list[i].text) else target
                else:
                    target = i
            hosts_file[domain] = ip if target else 'unreachable'

    with open('result.txt', 'w', encoding='utf-8') as r:
        for key in hosts_file:
            r.write(hosts_file[key] + ' ' + key + '\n')
    driver.close()


if __name__ == '__main__':
    main()
