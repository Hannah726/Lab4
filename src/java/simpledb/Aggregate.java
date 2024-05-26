import numpy as np
from scipy.stats import norm

# 定义期权价格计算函数
def option_price(S, K, r, T, sigma, option_type):
    d1 = (np.log(S / K) + (r + 0.5 * sigma**2) * T) / (sigma * np.sqrt(T))
    d2 = d1 - sigma * np.sqrt(T)
    
    if option_type == 'call':
        option_price = S * norm.cdf(d1) - K * np.exp(-r * T) * norm.cdf(d2)
    elif option_type == 'put':
        option_price = K * np.exp(-r * T) * norm.cdf(-d2) - S * norm.cdf(-d1)
    
    return option_price

# 定义计算隐含波动率的函数
def implied_volatility(S, K, r, T, option_price, option_type, tol=1e-6, max_iter=1000):
    # 初始的波动率估计
    sigma_low = 0.01
    sigma_high = 1.0
    
    # 二分法求解
    for i in range(max_iter):
        sigma = (sigma_low + sigma_high) / 2
        price_est = option_price(S, K, r, T, sigma, option_type)
        error = price_est - option_price
        
        if abs(error) < tol:
            return sigma
        
        if error < 0:
            sigma_low = sigma
        else:
            sigma_high = sigma
    
    return None

# 给定的参数
S = 2.651  # 当前50ETF价格
K = 2.65   # 行权价
r = 0.03   # 无风险利率
T = 28 / 365  # 到期日为28天
call_price = 0.1205  # 看涨期权价格
put_price = 0.1134   # 看跌期权价格

# 计算看涨期权的隐含波动率
implied_vol_call = implied_volatility(S, K, r, T, call_price, 'call')
print("看涨期权的隐含波动率:", implied_vol_call)

# 计算看跌期权的隐含波动率
implied_vol_put = implied_volatility(S, K, r, T, put_price, 'put')
print("看跌期权的隐含波动率:", implied_vol_put)
