require 'headless'
require 'selenium-webdriver'
require 'minitest/autorun'

describe 'Petlinic' do
  before do
    @headless = Headless.new
    @headless.start

    @driver = Selenium::WebDriver.for :firefox
    @driver.navigate.to 'http://tomcat:8080/petclinic'
    @driver.manage.timeouts.implicit_wait = 30
  end

  after do
    @headless.destroy
  end

  describe 'when homepage is available' do
    it 'should show correct page title' do
        sleep 3
        assert @driver.title == 'PetClinic :: a Spring Framework demonstration'
    end
  end

  describe 'when homepage is available' do
    it 'should have Find Owners page' do
        @driver.find_element(:class, 'icon-search').click
        sleep 3
        h2 = @driver.find_element(:tag_name, 'h2')
        assert h2.text == 'Find Owners'
    end
  end

  describe 'when homepage is available' do
      it 'should have veterinarians page' do
          @driver.find_element(:class, 'icon-th-list').click
          sleep 3
          h2 = @driver.find_element(:tag_name, 'h2')
          assert h2.text == 'Veterinarians'
      end
  end

  describe 'when homepage is available' do
      it 'should have search for veterinarian feature' do
          @driver.find_element(:class, 'icon-th-list').click
          @driver.find_element(:tag_name, 'input').send_keys('Helen Leary')
          result = @driver.find_element(:css, 'td.sorting_1').text
          assert result == 'Helen Leary'
      end
  end
end
