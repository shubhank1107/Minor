import requests
import bs4

def get_balance_sheet(url):
    html=requests.get(url)
    soup=bs4.BeautifulSoup(html.text,'html.parser')
    para = soup.select('.rangesection p')
    #for para in paragraphs:
    day_low = para[0].string + ' : ' + para[2].string 
    day_high = para[1].string + ' : ' + para[3].string
    temp = para[6].string
    temp = temp.replace('\n','')
    temp = temp.replace('\t','')
    temp = temp.replace('\r','')
    week_low = para[4].string + ' : ' + temp 
    week_low = week_low.replace('52 ','')
    temp = para[7].string
    temp = temp.replace('\n','')
    temp = temp.replace('\t','')
    temp = temp.replace('\r','')
    week_high = para[5].string + ' : ' + temp
    week_high = week_high.replace('52 ','')
	
    data = soup.select('.innerbar')
    tag1 = data[0]
    tag2 = data[1]
    day_avg_perc = tag1['style']
    day_avg_perc = day_avg_perc.replace('width:','')
    
    week_avg_perc = tag2['style']
    week_avg_perc = week_avg_perc.replace('width:','')
    
    data1 = soup.select('.pricewrap p')
    budget_last = data1[0].string + data1[1].string
    
    data2 = soup.select('.lastpricedetails p')
    volume = data2[3].get_text()
    volume = volume.replace('Volume ','')
	
    data3 = soup.select('.prevclose p')
    prevclose = data3[1].get_text()
    prevclose = prevclose.replace('\t','')
    prevclose = prevclose.replace('\n','')
    prevclose = prevclose.replace('\r','')
    
    
	
	
    print '\n'
    print '>>->> -------------- Basic Stats --------------- >'
    print '\n'
    print '>>------ Budget ------'
    print 'Last Budget : '+ budget_last
    print '>>------ Volume ------'
    print 'Total Volume : '+ volume
    print '>>------ Previous Close ------'
    print 'Last Close : ' + prevclose
    print '>>------ Daily Stats ------'
    print day_low
    print day_high
    print 'day average : ' + day_avg_perc
    print '>>------ 52 Weeks Stats ------'
    print week_low
    print week_high
    print 'week average : ' + week_avg_perc
    print '\n'
    print '>>->> -------------- Balance Sheet --------------- >'
    print '\n'
    print '>>------ Assets and Liabilities ------'
    
    data4 = soup.select('.crDataTable tbody')
    for i in data4:
     print i.get_text()
     print '\n'
	 
    data5 = soup.select('.mainRow td')
    for i in data5:
     print i.get_text()
	
    
	
	#   data=soup.select("#user-info",limit=1)

   # for i in data:
    #    name=i.text.encode('ascii','ignore')

    #return name
	

print '-------------------- IBM BALANCE SHEET --------------------------'
get_balance_sheet('http://www.marketwatch.com/investing/stock/ibm/financials/balance-sheet')
print '-------------------- CISCO BALANCE SHEET --------------------------'
get_balance_sheet('http://www.marketwatch.com/investing/stock/csco/financials/balance-sheet')
print '-------------------- INFOSYS BALANCE SHEET --------------------------'
get_balance_sheet('http://www.marketwatch.com/investing/stock/infy/financials/balance-sheet')
print '-------------------- AMAZON BALANCE SHEET --------------------------'
get_balance_sheet('http://www.marketwatch.com/investing/stock/amzn/financials/balance-sheet')
print '-------------------- GOOGLE BALANCE SHEET --------------------------'
get_balance_sheet('http://www.marketwatch.com/investing/stock/goog/financials/balance-sheet')
print '-------------------- FACEBOOK BALANCE SHEET --------------------------'
get_balance_sheet('http://www.marketwatch.com/investing/stock/fb/financials/balance-sheet')
print '-------------------- APPLE BALANCE SHEET --------------------------'
get_balance_sheet('http://www.marketwatch.com/investing/stock/aapl/financials/balance-sheet')
print '-------------------- COGNIZENT BALANCE SHEET --------------------------'
get_balance_sheet('http://www.marketwatch.com/investing/stock/ctsh/financials/balance-sheet')
print '-------------------- LINKEDIN BALANCE SHEET --------------------------'
get_balance_sheet('http://www.marketwatch.com/investing/stock/lnkd/financials/balance-sheet')
print '-------------------- NINTENDO BALANCE SHEET --------------------------'
get_balance_sheet('http://www.marketwatch.com/investing/stock/ntdoy/financials/balance-sheet')

raw_input()
