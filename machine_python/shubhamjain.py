import requests
import bs4
def getstocks(url):
    html = requests.get(url)
    soup = bs4.BeautifulSoup(html.text,'html.parser')
    
    data = soup.select('td.rgt')
    name = ""
    for i in data:
      name = name + i.text.encode('ascii','ignore')
      name = name + '^'
    high = name.split('^')
    count = 0
    for i in high:
      print i
      count = count + 1

    return count/5

'''
    data = soup.select('#quotedata div img')
    name = ""
    for i in data:
      temp = i.get('src')
      ary = temp.split('=')
      x = ary[2]
      price = x[0:-1]
      name = name + price
      name = name + '^'
    cur_price = name.split('^')
    
    for i in cur_price:
      print i   
'''

c = 0
c = c + getstocks('https://www.google.com/finance/historical?q=NYSE:BAC')  
t = 1
for i in range(8):
  front_url = 'https://www.google.com/finance/historical?q=NYSE%3ABAC&start='
  back_url = '&num=30&ei=9AsFWNmXNNT0uATu5YzoCA'
  url = front_url + str(t*30) + back_url
  c = c + getstocks(url)
  t = t + 1
print c