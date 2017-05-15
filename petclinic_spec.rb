require 'headless'
require 'selenium-webdriver'
require 'minitest/spec'
require 'minitest/autorun'

describe 'Petlinic' do
  before do
    @headless = Headless.new
    @headless.start

    @driver = Selenium::WebDriver.for :firefox
    @driver.navigate.to 'http://tomcat:8080/petclinic'
    @wait = Selenium::WebDriver::Wait.new(:timeout => 30)
    @driver.manage.timeouts.implicit_wait = 30
  end

  after do
    @headless.destroy
  end

  describe 'when homepage is available' do
    it 'I should see page title containing PetClinic' do
        sleep 3
        assert @driver.title == "PetClinic :: a Spring Framework demonstration"
    end
  end

  describe 'when homepage is available' do
    it 'click the find owners link' do
        @driver.find_element(:class, "icon-search").click
        sleep 3
        h2 = @driver.find_element(:tag_name, "h2")
        assert h2.text == "Find Owners"
    end
  end

  describe 'when homepage is available' do
      it 'click the veterinarians link' do
          @driver.find_element(:class, "icon-th-list").click
          sleep 3
          h2 = @driver.find_element(:tag_name, "h2")
          assert h2.text == "Veterinarians"
      end
  end

  describe 'when homepage is available' do
      it 'search for veterinarian' do
          @driver.find_element(:class, "icon-th-list").click
          search_box = @driver.find_element(:id, "vets_filter")
          textBox = search_box.find_element(:tag_name, "input")
          textBox.send_keys("Helen Leary")
          vets = @driver.find_element(:id, "vets").text
          assert vets.split("\n")[1] == "Helen Leary radiology"
      end
  end

end
