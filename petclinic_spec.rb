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
      assert @driver.title == 'PetClinic :: a Spring Framework demonstration'
    end
  end

  describe 'when site is available' do
    it 'should have Find Owners page' do
      # click on Find Owners menu item
      @driver.find_element(:class, 'icon-search').click

      # wait to see Add Owner on the page that opens
      @driver.find_element(:link_text, 'Add Owner')

      # grab H2 content
      h2 = @driver.find_element(:tag_name, 'h2').text

      assert h2 == 'Find Owners'
    end
  end

  describe 'when site is available' do
    it 'should have Veterinarians page' do
      # click on Veterinarian menu item
      @driver.find_element(:class, 'icon-th-list').click

      # wait to see View as XML on the page that opens
      @driver.find_element(:link_text, 'View as XML')

      # grab H2 content
      h2 = @driver.find_element(:tag_name, 'h2').text

      assert h2 == 'Veterinarians'
    end
  end

  describe 'when site is available' do
    it 'should have Search for Veterinarian feature' do
      # click on Veterinarian menu item
      @driver.find_element(:class, 'icon-th-list').click

      # type Veterinarian name in search box
      @driver.find_element(:tag_name, 'input').send_keys('Helen Leary')

      # grab first cell content in filtered results
      result = @driver.find_element(:css, 'td.sorting_1').text

      assert result == 'Helen Leary'
    end
  end
end
